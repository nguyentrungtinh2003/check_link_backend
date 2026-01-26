package com.TrungTinhBackend.check_link_backend.service.user;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.dto.*;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.exception.NotFoundException;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import com.TrungTinhBackend.check_link_backend.service.email.MailService;
import com.TrungTinhBackend.check_link_backend.service.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MailService mailService;

    @Override
    public APIResponse register(RegisterDTO registerDTO, HttpServletRequest request) throws IOException {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(registerDTO.getEmail());
        if(user != null) {
            throw new NotFoundException("Người dùng đã tồn tại");
        }

        User user1 = new User();
        user1.setUsername(registerDTO.getUsername());
        user1.setEmail(registerDTO.getEmail());
        user1.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user1.setRawPassword(registerDTO.getPassword());
        user1.setRole(Role.USER);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());
        user1.setActive(false);

        userRepository.save(user1);

        sendOTP(user1.getEmail());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Register success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse login(LoginDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request, Authentication authentication) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByUsername(loginRequestDTO.getUsername());
        if(user == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()));

        String token = jwtUtils.generateToken(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setActive(user.isActive());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Login success");
        apiResponse.setToken(token);
        apiResponse.setData(userDTO);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse processOAuthPostLogin(User user) {
        APIResponse apiResponse = new APIResponse();

        User user1 = userRepository.findByEmail(user.getEmail());
        if (user1 == null) {

            User user2 = new User();
            user2.setUsername(user.getUsername());
            user2.setEmail(user.getEmail());
            user2.setPassword(passwordEncoder.encode(user.getPassword()));
            user2.setRawPassword(user.getPassword());
            user2.setRole(Role.USER);
            user2.setImg(user.getImg());
            user2.setCreatedAt(LocalDateTime.now());
            user2.setUpdatedAt(LocalDateTime.now());
            user2.setActive(true);

            userRepository.save(user2);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Save login google success");
            apiResponse.setData(user2);
            apiResponse.setTimestamp(LocalDateTime.now());
            return apiResponse;
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Save login google success");
        apiResponse.setData(user1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public APIResponse getUserInfo(String jwt) {
        APIResponse apiResponse = new APIResponse();

        String username = jwtUtils.extractUsername(jwt);
        User user = userRepository.findByUsername(username);

        if (jwt == null || !jwtUtils.isTokenValid(jwt, user)) {
            throw new BadCredentialsException("Token invalid");
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user info success !");
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    @CacheEvict(value = {"userPage","user"},allEntries = true)
    public APIResponse updateUser(Long id, User user, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        User user1 = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Khoông tìm thấy người dùng")
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

        if(authUser == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        if(!id.equals(authUser.getId()) && !authUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Bạn không có quyền chỉnh sửa");
        }

        if(user.getUsername() != null && !user.getUsername().isEmpty()) {
            user1.setUsername(user.getUsername());
        }

        if(user.getEmail() != null && !user.getEmail().isEmpty()) {
            user1.setEmail(user.getEmail());
        }

        user1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user1);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user1.getId());
        userDTO.setUsername(user1.getUsername());
        userDTO.setEmail(user1.getEmail());
        userDTO.setRole(user1.getRole());
        userDTO.setCreatedAt(user1.getCreatedAt());
        userDTO.setActive(user1.isActive());

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update user success");
        apiResponse.setData(userDTO);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getUserById(Long id, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

if(!authUser.getId().equals(id) && !authUser.getRole().equals(Role.ADMIN)) {
    throw new AccessDeniedException("Bạn không có quyền truy cập");
}

        User user1 = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Khoông tìm thấy người dùng")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user by id success");
        apiResponse.setData(user1);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse sendOTP(String email) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(3));
        userRepository.save(user);

//        mailService.sendMail(email,"Mã OTP của URL Checker","OTP : "+otp);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Send OTP success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse resetPassword(ResetPass resetPass) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(resetPass.getEmail());
        if(user == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        if(user.getOtp() == null || !user.getOtp().equals(resetPass.getOtp())) {
            throw new RuntimeException("Mã OTP không hợp lệ");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            throw new RuntimeException("Mã OTP đã hết hạn");
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setPassword(passwordEncoder.encode(resetPass.getNewPassword()));
        user.setRawPassword(resetPass.getNewPassword());

        userRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Reset pass success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse verifyAcc(VerifyAcc verifyAcc) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(verifyAcc.getEmail());
        if(user == null) {
            throw new NotFoundException("Không tìm thấy người dùng");
        }

        if(user.getOtp() == null || !user.getOtp().equals(verifyAcc.getOtp())) {
            throw new RuntimeException("Mã OTP không hợp lệ");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            throw new RuntimeException("Mã OTP đã hết hạn");
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setActive(true);

        userRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Verify account success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    @CacheEvict(value = {"userPage","user"},allEntries = true)
    public APIResponse toggleDelete(Long id, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

        if(!authUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Bạn không có quyền truy cập");
        }

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy người dùng")
        );

        user.setActive(!user.isActive());
        userRepository.save(user);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Toggle delete user success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    @Cacheable(value = {"userPage"})
    public APIResponse getUserByPage(String keyword, int page, int size, Authentication authentication) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<User> userPage;

        if(keyword == null) {
            userPage = userRepository.findAll(pageable);
        }else {
            userPage = userRepository.searchByUsernameOrEmail(keyword,pageable);
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get user page = "+page+" size = "+size+" success");
        apiResponse.setData(userPage);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

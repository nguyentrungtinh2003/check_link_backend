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
import org.springframework.security.authentication.AuthenticationManager;
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

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Login success");
        apiResponse.setToken(token);
        apiResponse.setData(user);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
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

        if(!id.equals(authUser.getId())) {
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

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Update user success");
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

        mailService.sendEmail(email,"Mã OTP của URL Checker","OTP : "+otp);

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
}

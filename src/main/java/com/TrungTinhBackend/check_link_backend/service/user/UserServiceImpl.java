package com.TrungTinhBackend.check_link_backend.service.user;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.dto.LoginDTO;
import com.TrungTinhBackend.check_link_backend.dto.RegisterDTO;
import com.TrungTinhBackend.check_link_backend.dto.ResetPass;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        userRepository.save(user1);

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
            throw new NotFoundException("User not found");
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
    public APIResponse sendOTP(String email) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new NotFoundException("User not found");
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
            throw new NotFoundException("User not found");
        }

        if(user.getOtp() == null || !user.getOtp().equals(resetPass.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
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
}

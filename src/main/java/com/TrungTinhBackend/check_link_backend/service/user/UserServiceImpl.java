package com.TrungTinhBackend.check_link_backend.service.user;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.dto.LoginDTO;
import com.TrungTinhBackend.check_link_backend.dto.RegisterDTO;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.exception.NotFoundException;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
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
        user1.setPassword(passwordEncoder.encode(registerDTO.getUsername()));
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
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

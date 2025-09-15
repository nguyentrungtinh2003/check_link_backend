package com.TrungTinhBackend.check_link_backend.service.user;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.dto.LoginDTO;
import com.TrungTinhBackend.check_link_backend.dto.RegisterDTO;
import com.TrungTinhBackend.check_link_backend.dto.ResetPass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface UserService {
    APIResponse register(RegisterDTO registerDTO, HttpServletRequest request) throws IOException;
    APIResponse login(LoginDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request, Authentication authentication);
    APIResponse sendOTP(String email);
    APIResponse resetPassword(ResetPass resetPass);
}

package com.TrungTinhBackend.check_link_backend.service.user;

import com.TrungTinhBackend.check_link_backend.dto.*;
import com.TrungTinhBackend.check_link_backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public interface UserService {
    APIResponse register(RegisterDTO registerDTO, HttpServletRequest request) throws IOException;
    APIResponse login(LoginDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request, Authentication authentication);
    APIResponse updateUser(Long id, User user, Authentication authentication) throws AccessDeniedException;
    APIResponse sendOTP(String email);
    APIResponse resetPassword(ResetPass resetPass);
    APIResponse verifyAcc(VerifyAcc verifyAcc);
    APIResponse toggleDelete(Long id, Authentication authentication) throws AccessDeniedException;
    APIResponse getUserByPage(String keyword, int page, int size, Authentication authentication);
}

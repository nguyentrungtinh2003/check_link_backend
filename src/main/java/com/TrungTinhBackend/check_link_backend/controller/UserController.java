package com.TrungTinhBackend.check_link_backend.controller;

import com.TrungTinhBackend.check_link_backend.dto.*;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/ping")
    public String pingServer() {
        return "Server is running";
    }

    @PostMapping("/auth/register")
    public ResponseEntity<APIResponse> register(@RequestBody RegisterDTO registerDTO, HttpServletRequest request, Authentication authentication) throws IOException {
        return ResponseEntity.ok(userService.register(registerDTO,request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response, HttpServletRequest request, Authentication authentication) {
        return ResponseEntity.ok(userService.login(loginDTO,response,request,authentication));
    }

    @PostMapping("/auth/send-otp")
    public ResponseEntity<APIResponse> sendOTP(@RequestParam String email) {
        return ResponseEntity.ok(userService.sendOTP(email));
    }

    @PostMapping("/auth/verify-acc")
    public ResponseEntity<APIResponse> verifyAcc(@RequestBody VerifyAcc verifyAcc) {
        return ResponseEntity.ok(userService.verifyAcc(verifyAcc));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<APIResponse> resetPassword(@RequestBody ResetPass resetPass) {
        return ResponseEntity.ok(userService.resetPassword(resetPass));
    }

    @PutMapping("/user/update/{id}")
    public ResponseEntity<APIResponse> updateUser(@PathVariable Long id,
                                                  @RequestBody User user,
                                                  Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(userService.updateUser(id,user,authentication));
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable Long id,
                                                  Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(userService.toggleDelete(id,authentication));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<APIResponse> getUserById(@PathVariable Long id,
                                                  Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getUserById(id,authentication));
    }

    @GetMapping("/user")
    public ResponseEntity<APIResponse> getHistoryByUserId(
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size,
                                                          Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getUserByPage(keyword,page,size,authentication));
    }
}

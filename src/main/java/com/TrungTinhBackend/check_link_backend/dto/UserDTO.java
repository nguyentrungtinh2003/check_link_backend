package com.TrungTinhBackend.check_link_backend.dto;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.entity.History;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String rawPassword;
    private Role role;

    private String otp;
    private LocalDateTime otpExpiry;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean active;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String password, String rawPassword, Role role, String otp, LocalDateTime otpExpiry, LocalDateTime createdAt, LocalDateTime updatedAt, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rawPassword = rawPassword;
        this.role = role;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

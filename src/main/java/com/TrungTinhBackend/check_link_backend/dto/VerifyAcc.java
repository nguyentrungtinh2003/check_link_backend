package com.TrungTinhBackend.check_link_backend.dto;

import lombok.Data;

@Data
public class VerifyAcc {

    private String email;
    private String otp;

    public VerifyAcc() {
    }

    public VerifyAcc(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}

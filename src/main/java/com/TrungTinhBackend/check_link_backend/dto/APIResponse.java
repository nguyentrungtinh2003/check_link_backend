package com.TrungTinhBackend.check_link_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class APIResponse {

    private Long statusCode;
    private String message;
    private String googleSafeBrowsing;
    private Map<String,Object> virusTotal;
    private String fishTank;
    private String token;
    private Object data;
    private LocalDateTime timestamp;

    public APIResponse() {
    }

    public APIResponse(Long statusCode, String message, String googleSafeBrowsing, Map<String, Object> virusTotal, String fishTank, String token, Object data, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.googleSafeBrowsing = googleSafeBrowsing;
        this.virusTotal = virusTotal;
        this.fishTank = fishTank;
        this.token = token;
        this.data = data;
        this.timestamp = timestamp;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoogleSafeBrowsing() {
        return googleSafeBrowsing;
    }

    public void setGoogleSafeBrowsing(String googleSafeBrowsing) {
        this.googleSafeBrowsing = googleSafeBrowsing;
    }

    public Map<String, Object> getVirusTotal() {
        return virusTotal;
    }

    public void setVirusTotal(Map<String, Object> virusTotal) {
        this.virusTotal = virusTotal;
    }

    public String getFishTank() {
        return fishTank;
    }

    public void setFishTank(String fishTank) {
        this.fishTank = fishTank;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

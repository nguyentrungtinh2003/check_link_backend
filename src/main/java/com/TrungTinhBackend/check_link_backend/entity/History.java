package com.TrungTinhBackend.check_link_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String urlCheck;
    private String ipAddress;
    @Column(columnDefinition = "TEXT")
    private String userAgent;
    @Column(columnDefinition = "TEXT")
    private String googleSafeBrowsing;
    @Column(columnDefinition = "TEXT") // hoặc JSON nếu DB hỗ trợ
    private String virusTotal;

    @ManyToOne()
    @JoinColumn(name = "user_id",nullable = true)
    @JsonIgnore()
    private User user;

    private LocalDateTime createdAt;

    public History() {
    }

    public History(Long id, String urlCheck, String ipAddress, String userAgent, String googleSafeBrowsing, String virusTotal, User user, LocalDateTime createdAt) {
        this.id = id;
        this.urlCheck = urlCheck;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.googleSafeBrowsing = googleSafeBrowsing;
        this.virusTotal = virusTotal;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlCheck() {
        return urlCheck;
    }

    public void setUrlCheck(String urlCheck) {
        this.urlCheck = urlCheck;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getGoogleSafeBrowsing() {
        return googleSafeBrowsing;
    }

    public void setGoogleSafeBrowsing(String googleSafeBrowsing) {
        this.googleSafeBrowsing = googleSafeBrowsing;
    }

    public String getVirusTotal() {
        return virusTotal;
    }

    public void setVirusTotal(String virusTotal) {
        this.virusTotal = virusTotal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

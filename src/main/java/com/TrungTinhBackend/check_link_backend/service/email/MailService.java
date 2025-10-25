package com.TrungTinhBackend.check_link_backend.service.email;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    @Value("${mailersend.api.key}")
    private String api_key;

    private final String API_URL = "https://api.mailersend.com/v1/email";
    private final String API_KEY = api_key; // ⚠️ thay bằng API key của bạn
    private final String FROM_EMAIL = "test-3m5jgrok2xdgdpyo.mlsender.net"; // ⚠️ email đã xác thực trong MailerSend

    public void sendMail(String to,String subject,String text) {
        RestTemplate restTemplate = new RestTemplate();

        // ✅ Tạo nội dung email
        Map<String, Object> body = new HashMap<>();
        body.put("from", Map.of("email", FROM_EMAIL, "name", "URL Checker"));
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", text);

        // ✅ Header (chứa API Key)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        // ✅ Gửi POST request đến MailerSend API
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            System.out.println("✅ Gửi mail thành công: " + response.getBody());
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi gửi mail: " + e.getMessage());
        }
    }

}

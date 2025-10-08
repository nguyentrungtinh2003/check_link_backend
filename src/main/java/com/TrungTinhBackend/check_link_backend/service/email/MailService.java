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
    private String apiKey;

    public void sendEmail(String toEmail, String subject, String text) {
        String url = "https://api.mailersend.com/v1/email";

        Map<String, Object> from = Map.of(
                "email", "test-3m5jgrok2xdgdpyo.mlsender.net",  // bạn có thể dùng địa chỉ sandbox này
                "name", "Url-Checker"
        );

        Map<String, Object> to = Map.of(
                "email", toEmail,
                "name", "Receiver"
        );

        Map<String, Object> data = new HashMap<>();
        data.put("from", from);
        data.put("to", List.of(to));
        data.put("subject", subject);
        data.put("text", text);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

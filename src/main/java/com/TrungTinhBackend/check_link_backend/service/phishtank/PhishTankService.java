package com.TrungTinhBackend.check_link_backend.service.phishtank;

import com.TrungTinhBackend.check_link_backend.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PhishTankService {
    @Autowired
    private ApiConfig apiConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    public String checkUrl(String url) {
        String apiUrl = "https://checkurl.phishtank.com/checkurl/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "MySpringApp/1.0");

        String body = "url=" + url +
                "&format=json" +
                "&app_key=YOUR_API_KEY" +
                "&email=YOUR_EMAIL";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return "Error: " + response.getStatusCode();
    }
}

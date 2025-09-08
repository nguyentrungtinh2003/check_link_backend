package com.TrungTinhBackend.check_link_backend.service.virustotal;

import com.TrungTinhBackend.check_link_backend.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VirusTotalService {

    @Autowired
    private ApiConfig apiConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> checkUrl(String url) {
        String apiUrl = "https://www.virustotal.com/api/v3/urls";

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apikey", apiConfig.getVirustotalApiKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("url=" + url, headers);

        // B1: gửi URL để lấy id
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            if (data != null) {
                String id = (String) data.get("id");

                // B2: gọi tiếp để lấy kết quả
                String analysisUrl = "https://www.virustotal.com/api/v3/analyses/" + id;
                HttpEntity<Void> getRequest = new HttpEntity<>(headers);

                ResponseEntity<Map> analysisResponse =
                        restTemplate.exchange(analysisUrl, HttpMethod.GET, getRequest, Map.class);

                if (analysisResponse.getStatusCode() == HttpStatus.OK && analysisResponse.getBody() != null) {
                    Map<String, Object> body = analysisResponse.getBody();

                    // ✅ attributes nằm trong "data"
                    Map<String, Object> dataMap = (Map<String, Object>) body.get("data");
                    if (dataMap != null) {
                        Map<String, Object> attributes = (Map<String, Object>) dataMap.get("attributes");
                        if (attributes != null) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("stats", attributes.get("stats"));
                            result.put("status", attributes.get("status"));
                            result.put("results", attributes.get("results")); // nếu cần chi tiết từng vendor
                            return result;
                        }
                    }
                }
            }
        }

        // fallback: trả về map báo lỗi
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Không lấy được kết quả từ VirusTotal");
        return error;
    }
}

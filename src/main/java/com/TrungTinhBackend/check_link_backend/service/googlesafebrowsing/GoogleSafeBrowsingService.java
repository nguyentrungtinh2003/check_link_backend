package com.TrungTinhBackend.check_link_backend.service.googlesafebrowsing;

import com.TrungTinhBackend.check_link_backend.config.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleSafeBrowsingService {
    @Autowired
    private ApiConfig apiConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    public String checkUrl(String url) {
        String apiUrl = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key="
                + apiConfig.getGoogleApiKey();

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, String> client = new HashMap<>();
        client.put("clientId", "url-checker");
        client.put("clientVersion", "1.0");

        Map<String, Object> threatInfo = new HashMap<>();
        threatInfo.put("threatTypes", Arrays.asList("MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"));
        threatInfo.put("platformTypes", Collections.singletonList("ANY_PLATFORM"));
        threatInfo.put("threatEntryTypes", Collections.singletonList("URL"));
        threatInfo.put("threatEntries", Collections.singletonList(Collections.singletonMap("url", url)));

        requestBody.put("client", client);
        requestBody.put("threatInfo", threatInfo);

        Map response = restTemplate.postForObject(apiUrl, requestBody, Map.class);

        if (response != null && response.containsKey("matches")) {
            return "Không an toàn (Google Safe Browsing)";
        }
        return "An toàn (Google Safe Browsing)";
    }
}

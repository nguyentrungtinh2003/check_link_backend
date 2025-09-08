package com.TrungTinhBackend.check_link_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${virustotal.api.key}")
    private String virustotalApiKey;

    @Value("${phishtank.api.url}")
    private String phishTankUrl;

    public String getGoogleApiKey() {
        return googleApiKey;
    }

    public String getVirustotalApiKey() {
        return virustotalApiKey;
    }

    public String getPhishTankUrl() {
        return phishTankUrl;
    }
}

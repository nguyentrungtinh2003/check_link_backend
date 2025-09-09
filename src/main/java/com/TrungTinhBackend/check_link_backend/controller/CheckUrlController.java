package com.TrungTinhBackend.check_link_backend.controller;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.service.googlesafebrowsing.GoogleSafeBrowsingService;
import com.TrungTinhBackend.check_link_backend.service.phishtank.PhishTankService;
import com.TrungTinhBackend.check_link_backend.service.virustotal.VirusTotalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/check")
public class CheckUrlController {
    @Autowired
    private GoogleSafeBrowsingService googleSafeBrowsingService;

    @Autowired
    private VirusTotalService virusTotalService;

    @Autowired
    private PhishTankService phishTankService;

    @GetMapping
    public APIResponse checkAll(@RequestParam String url) throws JsonProcessingException {
        String googleResult = googleSafeBrowsingService.checkUrl(url);
        Map<String, Object> vtResult = virusTotalService.checkUrl(url);
//        String ptResult = phishTankService.checkUrl(url);

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Check success");
        apiResponse.setGoogleSafeBrowsing(googleResult);
        apiResponse.setVirusTotal(vtResult);
//        apiResponse.setFishTank(ptResult);

        return apiResponse;
    }
}

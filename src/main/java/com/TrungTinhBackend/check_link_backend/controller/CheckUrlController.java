package com.TrungTinhBackend.check_link_backend.controller;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import com.TrungTinhBackend.check_link_backend.service.googlesafebrowsing.GoogleSafeBrowsingService;
import com.TrungTinhBackend.check_link_backend.service.history.HistoryService;
import com.TrungTinhBackend.check_link_backend.service.phishtank.PhishTankService;
import com.TrungTinhBackend.check_link_backend.service.virustotal.VirusTotalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public APIResponse checkAll(
            @RequestParam String url,
            HttpServletRequest request,
            Authentication authentication) throws JsonProcessingException {

        String googleResult = googleSafeBrowsingService.checkUrl(url);
        Map<String, Object> vtResult = virusTotalService.checkUrl(url);

        Long userId = null; // mặc định là null

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername());
            if (user != null) {
                userId = user.getId();
            }
        }

        if (vtResult != null) {
            Object statusObj = vtResult.get("status");
            String status = statusObj != null ? statusObj.toString() : "";

            if (!"queued".equalsIgnoreCase(status)) {
                // Chỉ lưu nếu không phải "queued"
                historyService.addHistory(
                        userId,
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent"),
                        url,
                        googleResult,
                        vtResult
                );
            } else {
                System.out.println("⏳ VirusTotal đang xử lý, chưa lưu history.");
            }
        }

        APIResponse apiResponse = new APIResponse();
        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Check success");
        apiResponse.setGoogleSafeBrowsing(googleResult);
        apiResponse.setVirusTotal(vtResult);

        return apiResponse;
    }

}

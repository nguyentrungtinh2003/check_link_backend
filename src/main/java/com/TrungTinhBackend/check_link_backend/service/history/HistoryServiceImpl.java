package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.entity.History;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.exception.NotFoundException;
import com.TrungTinhBackend.check_link_backend.repository.HistoryRepository;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryServiceImpl implements HistoryService{

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public APIResponse addHistory(Long userId, String ipAddress, String userAgent, String urlCheck, String googleSafeBrowsing, String virusTotal) {
        APIResponse apiResponse = new APIResponse();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        History history = new History();
        history.setUrlCheck(urlCheck);
        history.setIpAddress(ipAddress);
        history.setUserAgent(userAgent);
        history.setGoogleSafeBrowsing(googleSafeBrowsing);
        history.setVirusTotal(virusTotal);
        history.setUser(user);
        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Save history success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

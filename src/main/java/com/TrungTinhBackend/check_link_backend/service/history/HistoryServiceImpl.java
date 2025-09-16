package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.entity.History;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.exception.NotFoundException;
import com.TrungTinhBackend.check_link_backend.repository.HistoryRepository;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService{

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public APIResponse addHistory(Long userId, String ipAddress, String userAgent, String urlCheck, String googleSafeBrowsing, Map<String,Object> virusTotal) throws JsonProcessingException {
        APIResponse apiResponse = new APIResponse();

        History history = new History();
        history.setUrlCheck(urlCheck);
        history.setIpAddress(ipAddress);
        history.setUserAgent(userAgent);
        history.setGoogleSafeBrowsing(googleSafeBrowsing);

        ObjectMapper mapper = new ObjectMapper();
        String vtJson = mapper.writeValueAsString(virusTotal);
        history.setVirusTotal(vtJson);

        if(userId != null) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("User not found")
            );

            history.setUser(user);
        }

        history.setCreatedAt(LocalDateTime.now());

        historyRepository.save(history);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Save history success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getHistoryByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<History> histories = historyRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get history success");
        apiResponse.setData(histories);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse deleteHistory(Long id, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

        List<History> histories = historyRepository.findByUserId(authUser.getId());

        History history = historyRepository.findById(id).orElseThrow(
                () -> new NotFoundException("History not found")
        );

        if(!histories.contains(history)) {
            throw new AccessDeniedException("Bạn không có quyền xoá");
        }

        historyRepository.delete(history);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Delete history success");
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }
}

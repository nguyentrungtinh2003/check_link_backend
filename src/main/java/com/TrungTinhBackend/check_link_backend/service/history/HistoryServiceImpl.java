package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.entity.History;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.exception.NotFoundException;
import com.TrungTinhBackend.check_link_backend.repository.HistoryRepository;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                    () -> new NotFoundException("Không tìm thấy người dùng")
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
    public APIResponse getHistoryByUserId(Long userId,String keyword, int page, int size, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

        if(!authUser.getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền truy cập");
        }

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<History> historyPage;

        if(keyword == null) {
            historyPage = historyRepository.findAll(pageable);
        }else {
            historyPage = historyRepository.findByUserIdAndUrlCheckContainingIgnoreCase(userId,keyword,pageable);
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get history by page = "+page+" size = "+size+" success");
        apiResponse.setData(historyPage);
        apiResponse.setTimestamp(LocalDateTime.now());
        return apiResponse;
    }

    @Override
    public APIResponse getHistoryByPage(String keyword, int page, int size, Authentication authentication) throws AccessDeniedException {
        APIResponse apiResponse = new APIResponse();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User authUser = userRepository.findByUsername(userDetails.getUsername());

        if(!authUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Bạn không có quyền truy cập");
        }

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<History> historyPage;

        if(keyword == null) {
            historyPage = historyRepository.findAll(pageable);
        }else {
            historyPage = historyRepository.findByUrlCheckContainingIgnoreCase(keyword,pageable);
        }

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get history by page = "+page+" size = "+size+" success");
        apiResponse.setData(historyPage);
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
                () -> new NotFoundException("Không tìm thấy lịch sử")
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

package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;
import java.util.Map;

public interface HistoryService {
    APIResponse addHistory(Long userId,String ipAddress,String userAgent,String urlCheck,String googleSafeBrowsing, Map<String,Object> virusTotal) throws JsonProcessingException;
    APIResponse getHistoryByUserId(Long userId,String keyword, int page, int size, Authentication authentication) throws AccessDeniedException;
    APIResponse deleteHistory(Long id, Authentication authentication) throws AccessDeniedException;
}

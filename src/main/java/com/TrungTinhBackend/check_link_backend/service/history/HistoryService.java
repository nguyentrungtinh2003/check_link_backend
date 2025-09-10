package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface HistoryService {
    APIResponse addHistory(Long userId,String ipAddress,String userAgent,String urlCheck,String googleSafeBrowsing, Map<String,Object> virusTotal) throws JsonProcessingException;
    APIResponse getHistoryByUserId(Long userId);
}

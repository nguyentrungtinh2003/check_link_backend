package com.TrungTinhBackend.check_link_backend.service.history;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;

public interface HistoryService {
    APIResponse addHistory(Long userId,String ipAddress,String userAgent,String urlCheck,String googleSafeBrowsing, String virusTotal);
}

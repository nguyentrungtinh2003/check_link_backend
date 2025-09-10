package com.TrungTinhBackend.check_link_backend.controller;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoryByUserId(userId));
    }
}

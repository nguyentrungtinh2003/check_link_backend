package com.TrungTinhBackend.check_link_backend.controller;

import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse> getHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoryByUserId(userId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteHistory(@PathVariable Long id,
                                                     Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(historyService.deleteHistory(id,authentication));
    }
}

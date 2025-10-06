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
    public ResponseEntity<APIResponse> getHistoryByUserId(@PathVariable Long userId,
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size,
                                                          Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(historyService.getHistoryByUserId(userId,keyword,page,size,authentication));
    }

    @GetMapping("/page")
    public ResponseEntity<APIResponse> getHistoryByPage(
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size,
                                                          Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(historyService.getHistoryByPage(keyword,page,size,authentication));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteHistory(@PathVariable Long id,
                                                     Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.ok(historyService.deleteHistory(id,authentication));
    }
}

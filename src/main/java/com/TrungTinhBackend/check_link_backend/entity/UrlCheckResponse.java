package com.TrungTinhBackend.check_link_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlCheckResponse {
    private String url;
    private String status;
    private String details;
}

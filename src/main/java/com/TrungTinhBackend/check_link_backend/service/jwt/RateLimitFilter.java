package com.TrungTinhBackend.check_link_backend.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();
    private static final int LIMIT = 10; // tối đa 3 request
    private static final long WINDOW = 60; // 60 giây

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Bỏ qua cho auth endpoints
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Áp dụng rate limit cho các API khác
        String ip = request.getRemoteAddr();
        long now = Instant.now().getEpochSecond();

        requestCounts.putIfAbsent(ip, new RequestInfo(0, now));
        RequestInfo info = requestCounts.get(ip);

        synchronized (info) {
            if (now - info.timestamp >= WINDOW) {
                info.count = 0;
                info.timestamp = now;
            }
            info.count++;

            if (info.count > LIMIT) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                // quan trọng: set charset trước khi getWriter()
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=UTF-8");

                Map<String, String> body = new HashMap<>();
                body.put("error", "Too many requests");
                body.put("message", "Bạn chỉ được phép gửi tối đa " + LIMIT + " request / " + WINDOW + " giây");

                // dùng Jackson để serialize (tránh lỗi quote/escape)
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(response.getWriter(), body);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private static class RequestInfo {
        int count;
        long timestamp;
        RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}

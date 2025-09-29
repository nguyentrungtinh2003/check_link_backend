package com.TrungTinhBackend.check_link_backend.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final SecretKey key;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30*60*1000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7*24*60*60*1000;

    public JwtUtils( @Value("${secret_string}")
                     String secretString) {
        byte[] keyBytes = secretString.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority
        ).toList();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles",roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String,Object> claims,UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream().map(
                GrantedAuthority::getAuthority
        ).toList();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("roles",roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public List<GrantedAuthority> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            List<String> roles = claims.get("roles", List.class);
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Lỗi khi lấy role từ JWT: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpiration(token);
    }

    public boolean isTokenExpiration(String token) {
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}

package com.TrungTinhBackend.check_link_backend.config;

import com.TrungTinhBackend.check_link_backend.Enum.Role;
import com.TrungTinhBackend.check_link_backend.dto.APIResponse;
import com.TrungTinhBackend.check_link_backend.entity.User;
import com.TrungTinhBackend.check_link_backend.repository.UserRepository;
import com.TrungTinhBackend.check_link_backend.service.jwt.JwtAuthFilter;
import com.TrungTinhBackend.check_link_backend.service.jwt.JwtUtils;
import com.TrungTinhBackend.check_link_backend.service.jwt.RateLimitFilter;
import com.TrungTinhBackend.check_link_backend.service.jwt.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RateLimitFilter rateLimitFilter;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**","/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/oauth2/**",             // 👈 Cho phép truy cập OAuth2 endpoint
                                "/login/oauth2/**", "/robots.txt", "/api/ws/**",  "/api/user-google" ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/history/user/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/check/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN")
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"You are not authorized to access this resource\"}");
                        })
                )
        // 🔹 Sử dụng Bean thay vì tạo mới instance
                .oauth2Login(oauth -> oauth
                .successHandler((request, response, authentication) -> {
                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

                    Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
                    String email = (String) attributes.get("email");
                    String name = (String) attributes.get("name");
                    String picture = (String) attributes.get("picture");

                    User user = userRepository.findByEmail(email);
                    if (user == null) {
                        user = new User();
                        user.setUsername(name);
                        user.setEmail(email);
                        user.setPassword("12345678");
                        user.setRawPassword("12345678");
                        user.setRole(Role.USER);
                        user.setImg(picture);
                        user.setCreatedAt(LocalDateTime.now());
                        user.setUpdatedAt(LocalDateTime.now());
                        user.setActive(true);

                        userRepository.save(user);
                    }

                    String jwt = jwtUtils.generateToken(user);

// ✅ Ghi token vào cookie (hoặc đính kèm redirect URL)
                    ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                            .httpOnly(false) // nếu frontend cần đọc token
                            .secure(true)
                            .path("/")
                            .maxAge(24 * 60 * 60) // 7 ngày
                            .build();

                    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

// ✅ Redirect kèm token (frontend nhận token từ query param)
                    response.sendRedirect("https://url-checker-dev.vercel.app/login-success?token=" + jwt);

                })
        );
                httpSecurity.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);
                httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

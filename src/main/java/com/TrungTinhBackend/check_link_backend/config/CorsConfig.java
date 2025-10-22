package com.TrungTinhBackend.check_link_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("CORS Config Loaded");
                registry.addMapping("/**")
                        .allowedOrigins(
//                                "http://localhost:3000",
//                                "http://localhost:5173",
                                "https://url-checker-dev.vercel.app/"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type", "Cookie")
                        .exposedHeaders("Set-Cookie")
                        .allowCredentials(true);
            }

        };
    }
}

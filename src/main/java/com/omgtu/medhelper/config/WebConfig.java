package com.omgtu.medhelper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                    "https://web.telegram.org",
                    "http://localhost:8080",
                    "http://127.0.0.1:8080",
                    "https://t.me",
                    "https://telegram.org",
                    "http://localhost:3000",
                    "http://127.0.0.1:3000",
                    "https://*.ngrok-free.app",
                    "https://*.ngrok.io"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("X-Telegram-Init-Data", "X-Telegram-User-ID")
                .allowCredentials(true)
                .maxAge(3600);
    }
} 
package com.vault.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow all origins (use with caution)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Replace with the correct frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                .allowedHeaders("*")
                .allowCredentials(true); // If you need credentials (cookies, HTTP auth)
    }
}

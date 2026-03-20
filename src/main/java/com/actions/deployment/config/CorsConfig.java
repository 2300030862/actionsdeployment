package com.actions.deployment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration for allowing cross-origin requests
 */
@Configuration
@Slf4j
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Configuring CORS for Angular frontend");

        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4200",      // Angular dev server
                        "http://localhost:3000",      // Alternative frontend port
                        "http://127.0.0.1:4200",
                        "http://127.0.0.1:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

        log.info("CORS configuration completed");
    }
}


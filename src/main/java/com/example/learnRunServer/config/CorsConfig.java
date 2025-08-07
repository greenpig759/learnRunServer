package com.example.learnRunServer.config;

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
                registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS를 적용
                        .allowedOrigins("http://localhost:8080") // 이 도메인의 요청만 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true);
            }
        };
    }
}

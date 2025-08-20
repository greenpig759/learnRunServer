package com.example.learnRunServer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("learnRunServer 테스트")
                .description("swagger Authorize 버튼 추가")
                .version("V1.0.1");

        // 1. SecurityScheme - JWT 인증 스키마 정의 "jwtAuth"
        String jwtSchemeName = "jwtAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(jwtSchemeName)            // 스키마 이름
                .type(SecurityScheme.Type.HTTP) // HTTP 인증 방식 사용
                .scheme("bearer")               // Bearer 방식
                .bearerFormat("JWT");           // 토큰 형식 명시 (JWT)

        // 2. SecurityRequirement - 모든 api 요청에 적용
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(jwtSchemeName);

        // OpenAPI 객체에 구성 요소 등록
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(jwtSchemeName, securityScheme))
                .addSecurityItem(securityRequirement) // securityItem: 기본 인증 요구사항 등록
                .info(info);
    }


}

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

        // SecurityScheme
        String jwtSchemeName = "jwtAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // SecurityRequirement
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(jwtSchemeName, securityScheme))
                .addSecurityItem(securityRequirement)
                .info(info);
    }


}

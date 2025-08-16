package com.example.learnRunServer.config;

import com.example.learnRunServer.token.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable); // CSRF 보호 비활성화
        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/learnRun/user/login", "/learnRun/memo/save",
                                "/learnRun/memo/update", "/learnRun/memo/delete", "/learnRun/memo/all",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/webjars/**").permitAll() // 이것들 허용 해놓음
                                .anyRequest().authenticated()
                        //authenticated()로 하면 설정한 것 이외의 요청은 인증 필요, permitAll로 하면 필요 없음

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // 기본 로그인 필터 (Username..) 보다 먼저 JWT 필터를 적용시켜 토큰을 먼저 검증하는 흐름
        return http.build();
    }
}

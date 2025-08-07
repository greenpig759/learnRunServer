package com.example.learnRunServer.token;


import com.example.learnRunServer.token.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // 생성자
    public JwtAuthenticationFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    // JWT 필터
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
        throws ServletException, IOException{

        String token = resolveToken(request); // 헤더에서 JWT 문자열만 잘라옴
        log.info("Request URI: {}", request.getRequestURI()); // 요청 URI 로그 추가

        if (token == null) {
            log.warn("Authorization 헤더가 없거나 'Bearer ' 토큰이 아닙니다.");
        } else {
            log.info("추출된 토큰: {}", token);
            try {
                if (jwtProvider.validateToken(token)) {
                    log.info("토큰 유효성 검증 성공");
                    // 1. 토큰에서 사용자 ID 추출
                    Long userId = jwtProvider.getUserIdFromToken(token);
                    log.info("추출된 사용자 ID: {}", userId);

                    // 2. UserDetails와 권한 정보를 담은 Authentication 객체 생성
                    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userId.toString());
                    log.info("인증 사용자 조회 성공: {}", customUserDetails.getUsername());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null,
                            customUserDetails.getAuthorities()
                    );

                    // 3. Authentication 객체를 스프링 시큐리티의 SecurityContextHolder에 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("SecurityContextHolder에 인증 정보 저장 완료");
                } else {
                    log.warn("유효하지 않은 JWT 토큰입니다.");
                }
            } catch (Exception e) {
                log.error("JWT 필터 처리 중 예외 발생: {}", e.getMessage(), e);
                // 예외가 발생해도 필터 체인은 계속 진행해야 함
            }
        }

        filterChain.doFilter(request, response); // 필터 체인에 요청 계속 전달
    }

    // 요청의 헤더값을 읽음 Bearer 다음부터 7글자 잘라서 반환, 조건에 안 맞는다면 null 반환
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
package com.example.learnRunServer.token;


import com.example.learnRunServer.token.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        if(StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            // 토큰이 있고 + 토큰이 유효하다면
            // 1. 토큰에서 사용자 ID 추출
            Long userId = jwtProvider.getUserIdFromToken(token);
            // 2. UserDetails와 권한 정보를 담은 Authentication 객체 생성
            // 추출한 userId를 기반으로 CustomerUser를 가져온다
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userId.toString());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );

            // 3. Authentication 객체를 스프링 시큐리티의 SecurityContextHolder에 저장하여 인증 처리 완료
            SecurityContextHolder.getContext().setAuthentication(authentication);
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

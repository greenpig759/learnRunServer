package com.example.learnRunServer.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey secretKey;
    private final long accessTokenExpiration; // 1시간
    private final long refreshTokenExpiration; // 7일

    // 생성자
    public JwtProvider(@Value("${spring.jwt.secret}") String secret,
                       @Value("${spring.jwt.access-token-expiration}") long accessTokenExpiration,
                       @Value("${spring.jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // 엑세스 토큰 생성
    public String createAccessToken(Long userId, String role){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);
        return Jwts.builder() // 헤더는 옵션 없어서 자동으로 {"alg":"HS256", "typ": "JWT"}으로 설정
                .subject(userId.toString()) // 여부터, 유저 pk정보
                .claim("role", role) // 유저의 역할을 담는 커스텀 플레임
                .issuedAt(now) // 언제 만들어(발급되어)졌는지
                .expiration(expiryDate) // 만료 시간, 여까지 payload
                .signWith(secretKey) // 서명(signature)
                .compact();
    }

    // 리프레시 토큰 생성, 엑세스 토큰과 크게 다르진 않음
    public String createRefreshToken(Long userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token){
        try{
            Jwts.parser() // 파서 객체 생성
                    .verifyWith(secretKey) // 서명을 검증할 시크릿 키 등록
                    .build() // 파서 인스턴스 생성
                    .parseSignedClaims(token); // JWT 토큰을 서명까지 검증하며 파싱
            return true; // 예외 없다면 유효한 토근
        }catch(JwtException | IllegalArgumentException e){
            return false; // 예외 발생 시 false 반환
        }
    }

    public Long getRefreshTokenExpiration(){
        return refreshTokenExpiration;
    }

    // 토큰에서 유저 아이디 추출
    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser() // 파서 객체 생성
                .verifyWith(secretKey) // 서명을 검증할 시크릿 키 들록
                .build()// 파서 인스턴스 완성
                .parseSignedClaims(token) // JWT 파싱 + 서명, 유효성 검증
                .getPayload(); // JWT의 payload에서 Claims 객체 가져오기
        return Long.valueOf(claims.getSubject()); // Claims 내부에서 subject 값을 Long 타입으로 변환해서 봔환
    }
}

package com.example.learnRunServer.user.service;


import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.token.Entity.RefreshTokenEntity;
import com.example.learnRunServer.token.repository.TokenRepository;
import com.example.learnRunServer.user.DTO.UserDTO;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import com.example.learnRunServer.token.JwtAuthenticationFilter;
import com.example.learnRunServer.token.JwtProvider;
import com.example.learnRunServer.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenRepository tokenRepository;

    // UserDTO -> UserEntity 메서드
    public UserEntity toUserEntity(UserDTO userDTO){
        return UserEntity.builder()
                .kakaoId(userDTO.getKakaoId())
                .build();
    }

    // 로그인 기능, 사용자의 정보를 대조 후 있다면 토큰 주기, 없다면 등록 후 토큰 주기
    public TokenResponse login(UserDTO userDTO){
        // 1. 사용자의 정보를 조회
        Optional<UserEntity> optionalUser = userRepository.findByKakaoId(userDTO.getKakaoId());
        UserEntity userEntity;


        // 2-1. 만약 사용자의 정보가 있다면 기존 정보를 사용
        // 2-2. 사용자의 정보가 없다면 새로 등록
        userEntity = optionalUser.orElseGet(() -> userRepository.save(toUserEntity(userDTO)));

        // 토큰 발급
        String accessToken = jwtProvider.createAccessToken(userEntity.getId(), "ROLE_USER");
        String refreshToken = jwtProvider.createRefreshToken(userEntity.getId());

        // 리프레시 토큰의 경우 DB에 저장
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(Duration.ofMillis(jwtProvider.getRefreshTokenExpiration())))
                .revoked(false)
                .user(userEntity)
                .build();
        tokenRepository.save(refreshTokenEntity);

        // 토큰을 묶어서 리턴
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    // 로그아웃(요청 시 리프레시 토큰을 포함할것)
    public void logout(String refreshToken){
        tokenRepository.findByToken(refreshToken)
                .ifPresent(token -> tokenRepository.delete(token));
    }


    // 탈퇴
    @Transactional
    public void leaveUser(Long userId){
        // 1. 유저의 정보를 찾음
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + userId));

        // 2. 해당 유저의 정보를 사용하는 테이블들의 정보를 지우고 유저의 정보도 지움
        userRepository.delete(userEntity);
    }

}

package com.example.learnRunServer.token.Entity;


import com.example.learnRunServer.user.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token_table")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(nullable = false, unique = true)
    private String token; // 리프레시 토큰 문자열

    @Column(nullable = false)
    private LocalDateTime issuedAt; // 발급 시간

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 만료 시간

    @Column(nullable = false) // 토큰이 유효한지 표시
    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user; // 토큰을 가진 유저
}

package com.example.learnRunServer.user.repository;

import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByKakaoId(String kakaoId);
    Optional<UserEntity> findByUserId(Long userId);

    boolean existsByKakaoId(String kakaoId);
}

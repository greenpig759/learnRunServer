package com.example.learnRunServer.token.repository;

import com.example.learnRunServer.token.Entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity token(String token);
}

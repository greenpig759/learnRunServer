package com.example.learnRunServer.token.repository;

import com.example.learnRunServer.token.Entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}

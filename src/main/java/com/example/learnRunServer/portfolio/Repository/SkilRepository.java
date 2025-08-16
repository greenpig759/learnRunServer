package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.SkilEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkilRepository extends JpaRepository<SkilEntity, Long> {
    Optional<SkilEntity> findBySkilId(Long skilId);

    List<SkilEntity> findAllByUser_UserId(Long user);
}

package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.AwardEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AwardRepository extends JpaRepository<AwardEntity, Long> {
    Optional<AwardEntity> findByAwardId(Long awardId);
    List<AwardEntity> findByUser(UserEntity user);
}

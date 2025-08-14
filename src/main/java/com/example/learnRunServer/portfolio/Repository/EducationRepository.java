package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.EducationEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    Optional<EducationEntity> findByEducationId(Long educationId);
    List<EducationEntity> findByUserUserId(Long userId);
}

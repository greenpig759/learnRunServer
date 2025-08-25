package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.EducationEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    List<EducationEntity> findAllByUser_UserId(Long userId);
    Optional<EducationEntity> findByEducationIdAndUser_UserId(Long educationId, Long userId);
}

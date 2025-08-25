package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.QualificationsEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QualificationsRepository extends JpaRepository<QualificationsEntity, Long> {
    List<QualificationsEntity> findAllByUser_UserId(Long userId);
    Optional<QualificationsEntity> findByQualificationsIdAndUser_UserId(Long qualificationsId, Long userId);
}

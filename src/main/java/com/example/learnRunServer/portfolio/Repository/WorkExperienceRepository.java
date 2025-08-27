package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.DTO.WorkExperienceDTO;
import com.example.learnRunServer.portfolio.Entity.WorkExperienceEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkExperienceRepository extends JpaRepository<WorkExperienceEntity, Long> {
    List<WorkExperienceEntity> findAllByUser_Id(Long userId);
    Optional<WorkExperienceEntity> findByIdAndUser_Id(Long workExperienceId, Long userId);
}

package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
    Optional<ProjectEntity> findByProjectId(Long projectId);
    List<ProjectEntity> findAllByUser_UserId(Long userId);
}

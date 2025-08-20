package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.ProfileEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {
    Optional<ProfileEntity> findByUser_UserId(Long userId);
}

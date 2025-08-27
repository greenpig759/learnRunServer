package com.example.learnRunServer.portfolio.Repository;

import com.example.learnRunServer.portfolio.Entity.AwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardRepository extends JpaRepository<AwardEntity, Long> {
    List<AwardEntity> findAllByUser_Id(Long userId);
    Optional<AwardEntity> findByIdAndUser_Id(Long awardId, Long userId); // 조회와 검증 동시에
}

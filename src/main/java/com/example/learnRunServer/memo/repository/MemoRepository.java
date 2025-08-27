package com.example.learnRunServer.memo.repository;

import com.example.learnRunServer.memo.Entity.MemoEntity;
import com.example.learnRunServer.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {
    Optional<MemoEntity> findById(Long memoId);

    List<MemoEntity> findAllByUser(UserEntity user);
}

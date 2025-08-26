package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.AwardNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Entity.AwardEntity;
import com.example.learnRunServer.portfolio.Repository.AwardRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;
    private final UserRepository userRepository;

    // DTO -> Entity 변환 메서드
    public AwardEntity toEntity(AwardDTO dto, UserEntity user){
        return AwardEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .user(user)
                .build();
    }
    // Entity -> DTO 변환 메서드
    public AwardDTO toDTO(AwardEntity entity) {
        return AwardDTO.builder()
                .awardId(entity.getAwardId())
                .date(entity.getDate())
                .title(entity.getTitle())
                .build();
    }

    // 등록 서비스 메서드
    @Transactional
    public Long saveAward(AwardDTO awardDTO, Long userId){
        log.debug("Attempting to save award for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        AwardEntity awardEntity = toEntity(awardDTO, userEntity); // Entity로 변환
        AwardEntity saved = awardRepository.save(awardEntity);
        log.info("Award saved: awardId={}, userId={}", saved.getAwardId(), userId);

        return saved.getAwardId();
    }

    // 수정, 삭제에서 사용하는 중복된 AwardId 검증 로직 메서드 추출 + 사용자 ID 검증 로직 추가
    private AwardEntity findAwardByIdAndValidateUser(Long awardId, Long userId) {
        return awardRepository.findByAwardIdAndUser_UserId(awardId, userId)
                .orElseThrow(() -> new AwardNotFoundException("Award not found with id: " + awardId + " for the current user"));
    }

    // 수정 서비스 메서드
    @Transactional
    public void updateAward(Long awardId, AwardDTO awardDTO, Long userId){
        log.debug("Attempting to update awardId={}", awardId);
        AwardEntity awardEntity = findAwardByIdAndValidateUser(awardId, userId); // 사용자의 award인지 검증 및 조회

        // DTO의 값으로 엔티티의 필드를 업데이트.
        // 트랜잭션이 커밋될 때 JPA가 조회 시점의 버전과 현재 DB의 버전을 자동으로 비교하고,
        // 버전이 다르면 ObjectOptimisticLockingFailureException을 던짐.
        awardEntity.setDate(awardDTO.getDate());
        awardEntity.setTitle(awardDTO.getTitle());
        log.info("Award updated: awardId={}", awardId);
    }

    // 삭제 서비스 메서드
    @Transactional
    public void deleteAward(Long awardId, Long userId){
        log.debug("Attempting to delete awardId={}", awardId);
        AwardEntity awardEntity = findAwardByIdAndValidateUser(awardId, userId); // 추출 메서드 사용
        // 삭제에서도 version 확인?
        awardRepository.delete(awardEntity);
        log.info("Award deleted: awardId={}", awardId);
    }

    // 조회 서비스 메서드
    @Transactional(readOnly = true)
    public List<AwardDTO> getAwards(Long userId){
        log.debug("Attempting to get awards for userId={}", userId);

        List<AwardEntity> awardEntities = awardRepository.findAllByUser_UserId(userId);
        log.debug("Found {} awards for userId={}", awardEntities.size(), userId);

        return awardEntities.stream()
                .map(this::toDTO)               // DTO로 변환
                .collect(Collectors.toList());  // 리스트로 변환
    }
}

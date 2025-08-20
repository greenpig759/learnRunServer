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
@Transactional
public class AwardService {
    private final AwardRepository awardRepository;
    private final UserRepository userRepository;

    public AwardEntity toEntity(AwardDTO dto){
        return AwardEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .build();
    }

    public Long saveAward(AwardDTO awardDTO, Long userId){
        log.debug("Attempting to save award for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        AwardEntity awardEntity = toEntity(awardDTO);
        awardEntity.setUser(userEntity);
        awardRepository.save(awardEntity);
        log.info("Award saved: awardId={}, userId={}", awardEntity.getAwardId(), userId);

        return awardEntity.getAwardId();
    }

    public void updateAward(Long awardId, AwardDTO awardDTO){
        log.debug("Attempting to update awardId={}", awardId);
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                .orElseThrow(()-> new AwardNotFoundException("ID가 " + awardId + "인 수정할 수상경력을 찾을 수 없습니다."));

        awardEntity.setDate(awardDTO.getDate());
        awardEntity.setTitle(awardDTO.getTitle());
        log.info("Award updated: awardId={}", awardId);
    }

    public void deleteAward(Long awardId){
        log.debug("Attempting to delete awardId={}", awardId);
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                .orElseThrow(()-> new AwardNotFoundException("ID가 " + awardId + "인 삭제할 수상경력을 찾을 수 없습니다."));
        awardRepository.delete(awardEntity);
        log.info("Award deleted: awardId={}", awardId);
    }

    @Transactional(readOnly = true)
    public List<AwardDTO> getAwards(Long userId){
        log.debug("Attempting to get awards for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
        }

        List<AwardEntity> awardEntities = awardRepository.findAllByUser_UserId(userId);
        log.debug("Found {} awards for userId={}", awardEntities.size(), userId);

        return awardEntities.stream()
                .map(awardEntity -> AwardDTO.builder()
                        .awardId(awardEntity.getAwardId())
                        .date(awardEntity.getDate())
                        .title(awardEntity.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}

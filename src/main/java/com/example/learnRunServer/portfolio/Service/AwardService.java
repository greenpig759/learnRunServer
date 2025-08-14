package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ResourceNotFoundException;
import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Entity.AwardEntity;
import com.example.learnRunServer.portfolio.Repository.AwardRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;
    private final UserRepository userRepository;

    public AwardEntity toEntity(AwardDTO dto){
        return AwardEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .build();
    }

    public void saveAward(AwardDTO awardDTO, Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        AwardEntity awardEntity = toEntity(awardDTO);
        awardEntity.setUser(userEntity);
        awardRepository.save(awardEntity);
    }

    public void updateAward(Long awardId, AwardDTO awardDTO){
        // 1. DB에서 ID로 수상 경력 조회
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                // 2. 조회 결과가 없으면 ResourceNotFoundException 발생
                .orElseThrow(()-> new ResourceNotFoundException("ID가 " + awardId + "인 수정할 수상경력을 찾을 수 없습니다."));

        awardEntity.setDate(awardDTO.getDate());
        awardEntity.setTitle(awardDTO.getTitle());
        awardRepository.save(awardEntity);
    }

    public void deleteAward(Long awardId){
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                .orElseThrow(()-> new ResourceNotFoundException("ID가 " + awardId + "인 삭제할 수상경력을 찾을 수 없습니다."));
        awardRepository.delete(awardEntity);
    }

    public List<AwardDTO> getAwards(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        List<AwardEntity> awardEntities = awardRepository.findByUserUserId(userId);

        return awardEntities.stream()
                .map(awardEntity -> AwardDTO.builder()
                        .awardId(awardEntity.getAwardId())
                        .date(awardEntity.getDate())
                        .title(awardEntity.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}

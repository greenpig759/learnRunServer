package com.example.learnRunServer.portfolio.Service;

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

    // DTO -> Entity 변환
    public AwardEntity toEntity(AwardDTO dto){
        AwardEntity awardEntity = AwardEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .build();
        return awardEntity;
    }

    // 수상경력 추가
    public void saveAward(AwardDTO awardDTO, Long userId){
        AwardEntity awardEntity = toEntity(awardDTO);

        // 유저 엔티티 조회후 수상경력 엔티티에 포함시키기
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        awardEntity.setUser(userEntity); // 외래 키
        awardRepository.save(awardEntity);
    }

    // 수상경력 수정
    public void updateAward(Long awardId, AwardDTO awardDTO){
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 수상경력이 존재하지 않습니다"));

        awardEntity.setDate(awardDTO.getDate());
        awardEntity.setTitle(awardDTO.getTitle());
        awardRepository.save(awardEntity);
    }

    // 수상경력 삭제
    public void deleteAward(Long awardId){
        AwardEntity awardEntity = awardRepository.findByAwardId(awardId)
                .orElseThrow(()-> new IllegalArgumentException("해당 수상경력이 존재하지 않습니다"));
        awardRepository.delete(awardEntity);
    }

    // 수상경력 리스트 불러오기
    public List<AwardDTO> getAwards(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1:N 관계 -> List로 가져옴.
        List<AwardEntity> awardEntities = awardRepository.findByUser(userEntity);

        // awardEntity 리스트 dto로 변환해서 리턴
        return awardEntities.stream() // List를 stream으로 변환
                .map(awardEntity -> AwardDTO.builder() // .map(): Entity 요소를 dto로 변환
                        .awardId(awardEntity.getAwardId())
                        .date(awardEntity.getDate())
                        .title(awardEntity.getTitle())
                        .build())
                .collect(Collectors.toList()); // 다시 리스트 형태로 수집
    }
}

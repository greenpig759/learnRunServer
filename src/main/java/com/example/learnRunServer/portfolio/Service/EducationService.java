package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.EducationDTO;
import com.example.learnRunServer.portfolio.Entity.EducationEntity;
import com.example.learnRunServer.portfolio.Repository.EducationRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationService {
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    // toEntity 메서드
    public EducationEntity toEntity(EducationDTO dto){
        EducationEntity educationEntity = EducationEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
        return educationEntity;
    }

    // 학력 추가 서비스
    public void saveEducation(EducationDTO educationDTO, Long userId){
        EducationEntity educationEntity = toEntity(educationDTO);

        // 유저 아이디로 존재 확인
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        educationEntity.setUser(userEntity); // 외래키 지정
        educationRepository.save(educationEntity);
    }

    // 학력 수정 서비스
    public void updateEducation(Long educationId, EducationDTO educationDTO){
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new RuntimeException("해당 학력이 존재하지 않습니다."));

        educationEntity.setTitle(educationDTO.getTitle());
        educationEntity.setStartDate(educationDTO.getStartDate());
        educationEntity.setEndDate(educationDTO.getEndDate());
        educationRepository.save(educationEntity);
    }

    // 학력 제거 서비스
    public void deleteEducation(Long educationId){
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new RuntimeException("해당 학력이 존재하지 않습니다."));

        educationRepository.delete(educationEntity);
    }

    // 학력 리스트 불러오기 서비스

    public List<EducationDTO> getEducations(Long userId){
        // 유저 존재 확인
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<EducationEntity> educationEntities = educationRepository.findByUser(userEntity);

        // educationEntity 리스트 dto로 변환해서 리턴
        return educationEntities.stream() // 리스트를 스트림으로 변환
                .map(educationEntity -> EducationDTO.builder() // Entity 요소를 dto로 변환
                        .educationId(educationEntity.getEducationId())
                        .title(educationEntity.getTitle())
                        .startDate(educationEntity.getStartDate())
                        .endDate(educationEntity.getEndDate())
                        .build())
                .collect(Collectors.toList()); // 다시 리스트, DTO 형태로 수집
    }
}

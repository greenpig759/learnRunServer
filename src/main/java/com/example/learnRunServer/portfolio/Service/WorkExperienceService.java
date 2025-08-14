package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.WorkExperienceDTO;
import com.example.learnRunServer.portfolio.Entity.WorkExperienceEntity;
import com.example.learnRunServer.portfolio.Repository.WorkExperienceRepository;
import com.example.learnRunServer.user.Entity.UserEntity;

import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {
    private final WorkExperienceRepository workExperienceRepository;
    private final UserRepository userRepository;

    // toEntity 메서드
    public WorkExperienceEntity toEntity(WorkExperienceDTO dto){
        WorkExperienceEntity workExperienceEntity = WorkExperienceEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        return workExperienceEntity;
    }

    // 경력 추가 서비스 + 유저 탐색 + 외래키 지정
    public void saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long userId){
        WorkExperienceEntity workExperienceEntity = toEntity(workExperienceDTO);

        // 유저 아이디로 유저 탐색, 없으면 User not found 반환
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        workExperienceEntity.setUser(userEntity);
        workExperienceRepository.save(workExperienceEntity);
    }

    // 경력 수정 서비스
    public void updateWorkExperience(Long workExperienceId, WorkExperienceDTO workExperienceDTO){
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new RuntimeException("해당 경력이 존재하지 않습니다."));

        workExperienceEntity.setTitle(workExperienceDTO.getTitle());
        workExperienceEntity.setStartDate(workExperienceDTO.getStartDate());
        workExperienceEntity.setEndDate(workExperienceDTO.getEndDate());
        workExperienceRepository.save(workExperienceEntity);
    }

    // 경력 삭제 서비스
    public void deleteWorkExperience(Long workExperienceId){
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new RuntimeException("해당 경력이 존재하지 않습니다."));

        workExperienceRepository.delete(workExperienceEntity);
    }

    // 경력 리스트 불러오기 서비스
    public List<WorkExperienceDTO> getWorkExperience(Long userId){
        UserEntity userEntity = userRepository.findByUserId(userId) // userId로 userEntity 탐색
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<WorkExperienceEntity> workExperienceEntities = workExperienceRepository.findByUser(userEntity); // userEntity로 Entity를 리스트 형식으로 탐색

        return workExperienceEntities.stream() // Entity 리스트 스트림으로 분할
                .map(workExperienceEntity -> WorkExperienceDTO.builder() // Entity 형식 DTO로 바꿈
                        .workExperienceId(workExperienceEntity.getWorkExperienceId())
                        .title(workExperienceEntity.getTitle())
                        .startDate(workExperienceEntity.getStartDate())
                        .endDate(workExperienceEntity.getEndDate())
                        .build())
                .collect(Collectors.toList()); // 분할한 값들 리스트로 수집
    }
}

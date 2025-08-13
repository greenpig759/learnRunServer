package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ResourceNotFoundException;
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

    public WorkExperienceEntity toEntity(WorkExperienceDTO dto){
        return WorkExperienceEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public void saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        WorkExperienceEntity workExperienceEntity = toEntity(workExperienceDTO);
        workExperienceEntity.setUser(userEntity);
        workExperienceRepository.save(workExperienceEntity);
    }

    public void updateWorkExperience(Long workExperienceId, WorkExperienceDTO workExperienceDTO){
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + workExperienceId + "인 수정할 경력 정보를 찾을 수 없습니다."));

        workExperienceEntity.setTitle(workExperienceDTO.getTitle());
        workExperienceEntity.setStartDate(workExperienceDTO.getStartDate());
        workExperienceEntity.setEndDate(workExperienceDTO.getEndDate());
        workExperienceRepository.save(workExperienceEntity);
    }

    public void deleteWorkExperience(Long workExperienceId){
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + workExperienceId + "인 삭제할 경력 정보를 찾을 수 없습니다."));

        workExperienceRepository.delete(workExperienceEntity);
    }

    public List<WorkExperienceDTO> getWorkExperience(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        List<WorkExperienceEntity> workExperienceEntities = workExperienceRepository.findByUserUserId(userId);

        return workExperienceEntities.stream()
                .map(workExperienceEntity -> WorkExperienceDTO.builder()
                        .workExperienceId(workExperienceEntity.getWorkExperienceId())
                        .title(workExperienceEntity.getTitle())
                        .startDate(workExperienceEntity.getStartDate())
                        .endDate(workExperienceEntity.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }
}

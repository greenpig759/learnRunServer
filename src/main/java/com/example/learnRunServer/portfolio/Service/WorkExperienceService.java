package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.exception.WorkExperienceNotFoundException;
import com.example.learnRunServer.portfolio.DTO.WorkExperienceDTO;
import com.example.learnRunServer.portfolio.Entity.WorkExperienceEntity;
import com.example.learnRunServer.portfolio.Repository.WorkExperienceRepository;
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
public class WorkExperienceService {
    private final WorkExperienceRepository workExperienceRepository;
    private final UserRepository userRepository;

    public WorkExperienceEntity toEntity(WorkExperienceDTO dto, UserEntity user){
        return WorkExperienceEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .user(user)
                .build();
    }

    public WorkExperienceDTO toDTO(WorkExperienceEntity entity){
        return WorkExperienceDTO.builder()
                .workExperienceId(entity.getWorkExperienceId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    @Transactional
    public Long saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long userId){
        log.debug("Attempting to save work experience for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        WorkExperienceEntity workExperienceEntity = toEntity(workExperienceDTO, userEntity);
        WorkExperienceEntity saved = workExperienceRepository.save(workExperienceEntity);
        log.info("Work experience saved: workExperienceId={}, userId={}", saved.getWorkExperienceId(), userId);

        return saved.getWorkExperienceId();
    }

    private WorkExperienceEntity findWorkExperienceByIdAndValidateUser(Long workExperienceId, Long userId) {
        return workExperienceRepository.findByWorkExperienceIdAndUser_UserId(workExperienceId, userId)
                .orElseThrow(() -> new WorkExperienceNotFoundException("WorkExperience not found with id: " + workExperienceId + " for the current user"));
    }

    @Transactional
    public void updateWorkExperience(Long workExperienceId, WorkExperienceDTO workExperienceDTO, Long userId){
        log.debug("Attempting to update workExperienceId={}", workExperienceId);
        WorkExperienceEntity workExperienceEntity = findWorkExperienceByIdAndValidateUser(workExperienceId, userId);

        workExperienceEntity.setTitle(workExperienceDTO.getTitle());
        workExperienceEntity.setStartDate(workExperienceDTO.getStartDate());
        workExperienceEntity.setEndDate(workExperienceDTO.getEndDate());
        log.info("Work experience updated: workExperienceId={}", workExperienceId);
    }

    @Transactional
    public void deleteWorkExperience(Long workExperienceId, Long userId){
        log.debug("Attempting to delete workExperienceId={}", workExperienceId);
        WorkExperienceEntity workExperienceEntity = findWorkExperienceByIdAndValidateUser(workExperienceId, userId);

        workExperienceRepository.delete(workExperienceEntity);
        log.info("Work experience deleted: workExperienceId={}", workExperienceId);
    }

    @Transactional(readOnly = true)
    public List<WorkExperienceDTO> getWorkExperiences(Long userId){
        log.debug("Attempting to get work experiences for userId={}", userId);

        List<WorkExperienceEntity> workExperienceEntities = workExperienceRepository.findAllByUser_UserId(userId);
        log.debug("Found {} work experiences for userId={}", workExperienceEntities.size(), userId);

        return workExperienceEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

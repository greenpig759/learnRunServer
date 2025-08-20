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
@Transactional
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

    public Long saveWorkExperience(WorkExperienceDTO workExperienceDTO, Long userId){
        log.debug("Attempting to save work experience for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        WorkExperienceEntity workExperienceEntity = toEntity(workExperienceDTO);
        workExperienceEntity.setUser(userEntity);
        workExperienceRepository.save(workExperienceEntity);
        log.info("Work experience saved: workExperienceId={}, userId={}", workExperienceEntity.getWorkExperienceId(), userId);

        return workExperienceEntity.getWorkExperienceId();
    }

    public void updateWorkExperience(Long workExperienceId, WorkExperienceDTO workExperienceDTO){
        log.debug("Attempting to update workExperienceId={}", workExperienceId);
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new WorkExperienceNotFoundException("WorkExperience not found with id: " + workExperienceId));

        workExperienceEntity.setTitle(workExperienceDTO.getTitle());
        workExperienceEntity.setStartDate(workExperienceDTO.getStartDate());
        workExperienceEntity.setEndDate(workExperienceDTO.getEndDate());
        log.info("Work experience updated: workExperienceId={}", workExperienceId);
    }

    public void deleteWorkExperience(Long workExperienceId){
        log.debug("Attempting to delete workExperienceId={}", workExperienceId);
        WorkExperienceEntity workExperienceEntity = workExperienceRepository.findByWorkExperienceId(workExperienceId)
                .orElseThrow(() -> new WorkExperienceNotFoundException("WorkExperience not found with id: " + workExperienceId));

        workExperienceRepository.delete(workExperienceEntity);
        log.info("Work experience deleted: workExperienceId={}", workExperienceId);
    }

    @Transactional(readOnly = true)
    public List<WorkExperienceDTO> getWorkExperiences(Long userId){
        log.debug("Attempting to get work experiences for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        List<WorkExperienceEntity> workExperienceEntities = workExperienceRepository.findAllByUser_UserId(userId);
        log.debug("Found {} work experiences for userId={}", workExperienceEntities.size(), userId);

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

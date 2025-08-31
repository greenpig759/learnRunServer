package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.EducationNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.EducationDTO;
import com.example.learnRunServer.portfolio.Entity.EducationEntity;
import com.example.learnRunServer.portfolio.Repository.EducationRepository;
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
public class EducationService {
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    public EducationEntity toEntity(EducationDTO dto, UserEntity user){
        return EducationEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .user(user)
                .build();
    }

    public EducationDTO toDTO(EducationEntity entity){
        return EducationDTO.builder()
                .Id(entity.getId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    @Transactional
    public Long saveEducation(EducationDTO educationDTO, Long userId){
        log.debug("Attempting to save education for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        EducationEntity educationEntity = toEntity(educationDTO, userEntity);
        EducationEntity saved = educationRepository.save(educationEntity);
        log.info("Education saved: educationId={}, userId={}", saved.getId(), userId);

        return saved.getId();
    }

    private EducationEntity findEducationByIdAndValidateUser(Long educationId, Long userId) {
        return educationRepository.findByIdAndUser_Id(educationId, userId)
                .orElseThrow(() -> new EducationNotFoundException("Education not found with id: " + educationId));
    }

    @Transactional
    public void updateEducation(Long educationId, EducationDTO educationDTO, Long userId){
        log.debug("Attempting to update educationId={}", educationId);
        EducationEntity educationEntity = findEducationByIdAndValidateUser(educationId, userId);

        educationEntity.setTitle(educationDTO.getTitle());
        educationEntity.setStartDate(educationDTO.getStartDate());
        educationEntity.setEndDate(educationDTO.getEndDate());
        log.info("Education updated: educationId={}", educationId);
    }

    @Transactional
    public void deleteEducation(Long educationId, Long userId){
        log.debug("Attempting to delete educationId={}", educationId);
        EducationEntity educationEntity = findEducationByIdAndValidateUser(educationId, userId);

        educationRepository.delete(educationEntity);
        log.info("Education deleted: educationId={}", educationId);
    }

    @Transactional(readOnly = true)
    public List<EducationDTO> getEducations(Long userId){
        log.debug("Attempting to get educations for userId={}", userId);

        List<EducationEntity> educationEntities = educationRepository.findAllByUser_Id(userId);
        log.debug("Found {} educations for userId={}", educationEntities.size(), userId);

        return educationEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

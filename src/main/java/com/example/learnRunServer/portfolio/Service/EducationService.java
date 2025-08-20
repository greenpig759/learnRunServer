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
@Transactional
public class EducationService {
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    public EducationEntity toEntity(EducationDTO dto){
        return EducationEntity.builder()
                .title(dto.getTitle())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public Long saveEducation(EducationDTO educationDTO, Long userId){
        log.debug("Attempting to save education for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        EducationEntity educationEntity = toEntity(educationDTO);
        educationEntity.setUser(userEntity);
        educationRepository.save(educationEntity);
        log.info("Education saved: educationId={}, userId={}", educationEntity.getEducationId(), userId);

        return educationEntity.getEducationId();
    }

    public void updateEducation(Long educationId, EducationDTO educationDTO){
        log.debug("Attempting to update educationId={}", educationId);
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new EducationNotFoundException("ID가 " + educationId + "인 수정할 학력 정보를 찾을 수 없습니다."));

        educationEntity.setTitle(educationDTO.getTitle());
        educationEntity.setStartDate(educationDTO.getStartDate());
        educationEntity.setEndDate(educationDTO.getEndDate());
        log.info("Education updated: educationId={}", educationId);
    }

    public void deleteEducation(Long educationId){
        log.debug("Attempting to delete educationId={}", educationId);
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new EducationNotFoundException("ID가 " + educationId + "인 삭제할 학력 정보를 찾을 수 없습니다."));

        educationRepository.delete(educationEntity);
        log.info("Education deleted: educationId={}", educationId);
    }

    @Transactional(readOnly = true)
    public List<EducationDTO> getEducations(Long userId){
        log.debug("Attempting to get educations for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
        }

        List<EducationEntity> educationEntities = educationRepository.findAllByUser_UserId(userId);
        log.debug("Found {} educations for userId={}", educationEntities.size(), userId);

        return educationEntities.stream()
                .map(educationEntity -> EducationDTO.builder()
                        .educationId(educationEntity.getEducationId())
                        .title(educationEntity.getTitle())
                        .startDate(educationEntity.getStartDate())
                        .endDate(educationEntity.getEndDate())
                        .build())
                .collect(Collectors.toList());
    }
}

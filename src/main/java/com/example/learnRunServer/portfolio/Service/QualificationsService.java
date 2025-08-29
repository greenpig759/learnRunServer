package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.QualificationsNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.QualificationsDTO;
import com.example.learnRunServer.portfolio.Entity.QualificationsEntity;
import com.example.learnRunServer.portfolio.Repository.QualificationsRepository;
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
public class QualificationsService {
    private final QualificationsRepository qualificationsRepository;
    private final UserRepository userRepository;

    public QualificationsEntity toEntity(QualificationsDTO dto, UserEntity user){
        return QualificationsEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .user(user)
                .build();
    }

    public QualificationsDTO toDTO(QualificationsEntity entity){
        return QualificationsDTO.builder()
                .Id(entity.getId())
                .title(entity.getTitle())
                .date(entity.getDate())
                .build();
    }

    @Transactional
    public Long saveQualifications(QualificationsDTO qualificationsDTO, Long userId){
        log.debug("Attempting to save qualification for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        QualificationsEntity qualificationsEntity = toEntity(qualificationsDTO, userEntity);
        QualificationsEntity saved = qualificationsRepository.save(qualificationsEntity);
        log.info("Qualification saved: qualificationId={}, userId={}", saved.getId(), userId);
        return saved.getId();
    }

    private QualificationsEntity findQualificationByIdAndValidateUser(Long qualificationsId, Long userId) {
        return qualificationsRepository.findByIdAndUser_Id(qualificationsId, userId)
                .orElseThrow(() -> new QualificationsNotFoundException("Qualification not found with id: " + qualificationsId));
    }

    @Transactional
    public void updateQualifications(Long qualificationsId, QualificationsDTO qualificationsDTO, Long userId){
        log.debug("Attempting to update qualificationId={}", qualificationsId);
        QualificationsEntity qualificationsEntity = findQualificationByIdAndValidateUser(qualificationsId, userId);

        qualificationsEntity.setTitle(qualificationsDTO.getTitle());
        qualificationsEntity.setDate(qualificationsDTO.getDate());
        log.info("Qualification updated: qualificationId={}", qualificationsId);
    }

    @Transactional
    public void deleteQualifications(Long qualificationsId, Long userId){
        log.debug("Attempting to delete qualificationId={}", qualificationsId);
        QualificationsEntity qualificationsEntity = findQualificationByIdAndValidateUser(qualificationsId, userId);

        qualificationsRepository.delete(qualificationsEntity);
        log.info("Qualification deleted: qualificationId={}", qualificationsId);
    }

    @Transactional(readOnly = true)
    public List<QualificationsDTO> getQualifications(Long userId){
        log.debug("Attempting to get qualifications for userId={}", userId);

        List<QualificationsEntity> qualificationsEntities = qualificationsRepository.findAllByUser_Id(userId);
        log.debug("Found {} qualifications for userId={}", qualificationsEntities.size(), userId);

        return qualificationsEntities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

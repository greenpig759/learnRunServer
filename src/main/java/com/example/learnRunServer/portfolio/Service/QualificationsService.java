package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.QualificationNotFoundException;
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
@Transactional
public class QualificationsService {
    private final QualificationsRepository qualificationsRepository;
    private final UserRepository userRepository;

    public QualificationsEntity toEntity(QualificationsDTO dto){
        return QualificationsEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .build();
    }

    public Long saveQualifications(QualificationsDTO qualificationsDTO, Long userId){
        log.debug("Attempting to save qualification for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        QualificationsEntity qualificationsEntity = toEntity(qualificationsDTO);
        qualificationsEntity.setUser(userEntity);
        qualificationsRepository.save(qualificationsEntity);
        log.info("Qualification saved: qualificationId={}, userId={}", qualificationsEntity.getQualificationsId(), userId);
        return qualificationsEntity.getQualificationsId();
    }

    public void updateQualifications(Long qualificationsId, QualificationsDTO qualificationsDTO){
        log.debug("Attempting to update qualificationId={}", qualificationsId);
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new QualificationNotFoundException("ID가 " + qualificationsId + "인 수정할 자격증을 찾을 수 없습니다."));

        qualificationsEntity.setTitle(qualificationsDTO.getTitle());
        qualificationsEntity.setDate(qualificationsDTO.getDate());
        log.info("Qualification updated: qualificationId={}", qualificationsId);
    }

    public void deleteQualifications(Long qualificationsId){
        log.debug("Attempting to delete qualificationId={}", qualificationsId);
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new QualificationNotFoundException("ID가 " + qualificationsId + "인 삭제할 자격증을 찾을 수 없습니다."));

        qualificationsRepository.delete(qualificationsEntity);
        log.info("Qualification deleted: qualificationId={}", qualificationsId);
    }

    @Transactional(readOnly = true)
    public List<QualificationsDTO> getQualifications(Long userId){
        log.debug("Attempting to get qualifications for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
        }

        List<QualificationsEntity> qualificationsEntities = qualificationsRepository.findAllByUser_UserId(userId);
        log.debug("Found {} qualifications for userId={}", qualificationsEntities.size(), userId);

        return qualificationsEntities.stream()
                .map(qualificationsEntity -> QualificationsDTO.builder()
                        .qualificationsId(qualificationsEntity.getQualificationsId())
                        .date(qualificationsEntity.getDate())
                        .title(qualificationsEntity.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}

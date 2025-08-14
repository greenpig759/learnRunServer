package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ResourceNotFoundException;
import com.example.learnRunServer.portfolio.DTO.QualificationsDTO;
import com.example.learnRunServer.portfolio.Entity.QualificationsEntity;
import com.example.learnRunServer.portfolio.Repository.QualificationsRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QualificationsService {
    private final QualificationsRepository qualificationsRepository;
    private final UserRepository userRepository;

    public QualificationsEntity toEntity(QualificationsDTO dto){
        return QualificationsEntity.builder()
                .title(dto.getTitle())
                .date(dto.getDate())
                .build();
    }

    public void saveQualifications(QualificationsDTO qualificationsDTO, Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        QualificationsEntity qualificationsEntity = toEntity(qualificationsDTO);
        qualificationsEntity.setUser(userEntity);
        qualificationsRepository.save(qualificationsEntity);
    }

    public void updateQualifications(Long qualificationsId, QualificationsDTO qualificationsDTO){
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + qualificationsId + "인 수정할 자격증을 찾을 수 없습니다."));

        qualificationsEntity.setTitle(qualificationsDTO.getTitle());
        qualificationsEntity.setDate(qualificationsDTO.getDate());
        qualificationsRepository.save(qualificationsEntity);
    }

    public void deleteQualifications(Long qualificationsId){
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + qualificationsId + "인 삭제할 자격증을 찾을 수 없습니다."));

        qualificationsRepository.delete(qualificationsEntity);
    }

    public List<QualificationsDTO> getQualifications(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        List<QualificationsEntity> qualificationsEntities = qualificationsRepository.findByUserUserId(userId);

        return qualificationsEntities.stream()
                .map(qualificationsEntity -> QualificationsDTO.builder()
                        .qualificationsId(qualificationsEntity.getQualificationsId())
                        .date(qualificationsEntity.getDate())
                        .title(qualificationsEntity.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}

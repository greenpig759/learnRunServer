package com.example.learnRunServer.portfolio.Service;

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

    // toEntity 메서드
    public QualificationsEntity toEntity(QualificationsDTO dto){
        QualificationsEntity qualificationsEntity = QualificationsEntity.builder()
                .title(dto.getTitle())
                .qualificationsDate(dto.getQualificationsDate())
                .build();

        return qualificationsEntity;
    }

    // 자격증 추가 서비스 + 유저 탐색
    public void saveQualifications(QualificationsDTO qualificationsDTO, Long userId){
        QualificationsEntity qualificationsEntity = toEntity(qualificationsDTO);

        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        qualificationsEntity.setUser(userEntity); // 외래키 지정
        qualificationsRepository.save(qualificationsEntity);
    }

    // 자격증 수정 서비스
    public void updateQualifications(Long qualificationsId, QualificationsDTO qualificationsDTO){
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new RuntimeException("해당 자격증이 존재하지 않습니다."));

        qualificationsEntity.setTitle(qualificationsDTO.getTitle());
        qualificationsEntity.setQualificationsDate(qualificationsDTO.getQualificationsDate());
        qualificationsRepository.save(qualificationsEntity);
    }

    // 자격증 삭제 서비스
    public void deleteQualifications(Long qualificationsId){
        QualificationsEntity qualificationsEntity = qualificationsRepository.findByQualificationsId(qualificationsId)
                .orElseThrow(() -> new RuntimeException("해당 자격증이 존재하지 않습니다."));

        qualificationsRepository.delete(qualificationsEntity);
    }

    // 자격증 리스트 불러오기 서비스
    public List<QualificationsDTO> getQualifications(Long userId){
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<QualificationsEntity> qualificationsEntities = qualificationsRepository.findByUser(userEntity);

        return qualificationsEntities.stream()
                .map(qualificationsEntity -> QualificationsDTO.builder()
                        .qualificationsId(qualificationsEntity.getQualificationsId())
                        .qualificationsDate(qualificationsEntity.getQualificationsDate())
                        .title(qualificationsEntity.getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}

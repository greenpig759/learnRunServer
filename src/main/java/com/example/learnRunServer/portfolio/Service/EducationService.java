package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ResourceNotFoundException;
import com.example.learnRunServer.portfolio.DTO.EducationDTO;
import com.example.learnRunServer.portfolio.Entity.EducationEntity;
import com.example.learnRunServer.portfolio.Repository.EducationRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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

    public void saveEducation(EducationDTO educationDTO, Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        EducationEntity educationEntity = toEntity(educationDTO);
        educationEntity.setUser(userEntity);
        educationRepository.save(educationEntity);
    }

    public void updateEducation(Long educationId, EducationDTO educationDTO){
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + educationId + "인 삭제할 학력 정보를 찾을 수 없습니다."));

        educationEntity.setTitle(educationDTO.getTitle());
        educationEntity.setStartDate(educationDTO.getStartDate());
        educationEntity.setEndDate(educationDTO.getEndDate());
        educationRepository.save(educationEntity);
    }

    public void deleteEducation(Long educationId){
        EducationEntity educationEntity = educationRepository.findByEducationId(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + educationId + "인 삭제할 학력 정보를 찾을 수 없습니다."));

        educationRepository.delete(educationEntity);
    }

    public List<EducationDTO> getEducations(Long userId){
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        List<EducationEntity> educationEntities = educationRepository.findByUserUserId(userId);

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

package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.portfolio.DTO.ProfileDTO;
import com.example.learnRunServer.portfolio.Entity.ProfileEntity;
import com.example.learnRunServer.portfolio.Repository.ProfileRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // DTO -> Entity 변환
    public ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity profileEntity = ProfileEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
        return profileEntity;
    }


    // 프로필 추가
    public void saveProfile(ProfileDTO profileDTO, Long userId) {
        ProfileEntity profileEntity = toEntity(profileDTO);

        // 유저 엔티티 조회후 프로필 엔티티에 포함시키기
        UserEntity userEntity = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        profileRepository.save(profileEntity);
    }

    // 프로필 수정
    public void updateProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = profileRepository.findByProfileId(profileDTO.getProfileId())
                .orElseThrow(()-> new IllegalArgumentException("해당 프로필이 존재하지 않습니다"));

        profileEntity.setName(profileDTO.getName());
        profileEntity.setEmail(profileDTO.getEmail());
        profileRepository.save(profileEntity);
    }

    // 프로필 삭제
    public void deleteProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = profileRepository.findByProfileId(profileDTO.getProfileId())
                .orElseThrow(()-> new IllegalArgumentException("해당 프로필이 존재하지 않습니다"));
        profileRepository.delete(profileEntity);
    }

    // 프로필 불러오기
    public ProfileDTO getProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        // 일대일 관계이므로 하나만 가져옴
        ProfileEntity profileEntity = profileRepository.findByUser(userEntity)
                .orElseThrow(()-> new RuntimeException("Profile not found"));

        // DTO로 변환하여 반환
        ProfileDTO profileDTO = ProfileDTO.builder()
                .profileId(profileEntity.getProfileId())
                .name(profileEntity.getName())
                .email(profileEntity.getEmail())
                .build();
        return profileDTO;
    }
}

package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ProfileNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.ProfileDTO;
import com.example.learnRunServer.portfolio.Entity.ProfileEntity;
import com.example.learnRunServer.portfolio.Repository.ProfileRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // DTO -> Entity 변환
    public ProfileEntity toEntity(ProfileDTO dto) {
        return ProfileEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    // 프로필 추가
    public void saveProfile(ProfileDTO profileDTO, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setUser(userEntity);
        profileRepository.save(profileEntity);
    }

    // 프로필 수정
    public void updateProfile(ProfileDTO profileDTO, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        ProfileEntity profileEntity = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("ID가 " + userId + "인 수정할 프로필을 찾을 수 없습니다."));

        profileEntity.setName(profileDTO.getName());
        profileEntity.setEmail(profileDTO.getEmail());
        profileRepository.save(profileEntity);
    }

    // 프로필 삭제
    public void deleteProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        ProfileEntity profileEntity = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("ID가 " + userId + "인 삭제할 프로필을 찾을 수 없습니다."));

        profileRepository.delete(profileEntity);
    }

    // 프로필 불러오기
    public ProfileDTO getProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));

        ProfileEntity profileEntity = profileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("ID가 " + userId + "인 프로필을 찾을 수 없습니다."));

        // DTO로 변환하여 반환
        return ProfileDTO.builder()
                .profileId(profileEntity.getProfileId())
                .name(profileEntity.getName())
                .email(profileEntity.getEmail())
                .build();
    }
}

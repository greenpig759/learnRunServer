package com.example.learnRunServer.portfolio.Service;

import com.example.learnRunServer.exception.ProfileNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.portfolio.DTO.ProfileDTO;
import com.example.learnRunServer.portfolio.Entity.ProfileEntity;
import com.example.learnRunServer.portfolio.Repository.ProfileRepository;
import com.example.learnRunServer.user.Entity.UserEntity;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileEntity toEntity(ProfileDTO dto) {
        return ProfileEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public Long saveProfile(ProfileDTO profileDTO, Long userId) {
        log.debug("Attempting to save profile for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setUser(userEntity);
        profileRepository.save(profileEntity);
        log.info("Profile saved: profileId={}, userId={}", profileEntity.getProfileId(), userId);
        return profileEntity.getProfileId();
    }

    public void updateProfile(ProfileDTO profileDTO, Long userId) {
        log.debug("Attempting to update profile for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
        }

        ProfileEntity profileEntity = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("User not found with id: " + userId));

        profileEntity.setName(profileDTO.getName());
        profileEntity.setEmail(profileDTO.getEmail());
        log.info("Profile updated for userId={}", userId);
    }

    public void deleteProfile(Long userId) {
        log.debug("Attempting to delete profile for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
        }

        ProfileEntity profileEntity = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("User not found with id: " + userId));

        profileRepository.delete(profileEntity);
        log.info("Profile deleted for userId={}", userId);
    }

    @Transactional(readOnly = true)
    public ProfileDTO getProfile(Long userId) {
        log.debug("Attempting to get profile for userId={}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        ProfileEntity profileEntity = profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("User not found with id: " + userId));

        return ProfileDTO.builder()
                .profileId(profileEntity.getProfileId())
                .name(profileEntity.getName())
                .email(profileEntity.getEmail())
                .build();
    }
}

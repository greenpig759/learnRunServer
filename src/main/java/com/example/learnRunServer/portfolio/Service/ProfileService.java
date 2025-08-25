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
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileEntity toEntity(ProfileDTO dto, UserEntity user) {
        return ProfileEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .user(user)
                .build();
    }

    public ProfileDTO toDTO(ProfileEntity entity){
        return ProfileDTO.builder()
                .profileId(entity.getProfileId())
                .name(entity.getName())
                .email(entity.getEmail())
                .version(entity.getVersion())
                .build();
    }

    @Transactional
    public Long saveProfile(ProfileDTO profileDTO, Long userId) {
        log.debug("Attempting to save profile for userId={}", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        profileRepository.findByUser_UserId(userId).ifPresent(p -> {
            throw new IllegalStateException("Profile already exists for this user.");
        });

        ProfileEntity profileEntity = toEntity(profileDTO, userEntity);
        ProfileEntity saved = profileRepository.save(profileEntity);
        log.info("Profile saved: profileId={}, userId={}", saved.getProfileId(), userId);
        return saved.getProfileId();
    }

    private ProfileEntity findProfileByUserId(Long userId) {
        return profileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for user id: " + userId));
    }

    @Transactional
    public void updateProfile(ProfileDTO profileDTO, Long userId) {
        log.debug("Attempting to update profile for userId={}", userId);
        ProfileEntity profileEntity = findProfileByUserId(userId);

        profileEntity.setName(profileDTO.getName());
        profileEntity.setEmail(profileDTO.getEmail());
        log.info("Profile updated for userId={}", userId);
    }

    @Transactional
    public void deleteProfile(Long userId) {
        log.debug("Attempting to delete profile for userId={}", userId);
        ProfileEntity profileEntity = findProfileByUserId(userId);

        profileRepository.delete(profileEntity);
        log.info("Profile deleted for userId={}", userId);
    }

    @Transactional(readOnly = true)
    public ProfileDTO getProfile(Long userId) {
        log.debug("Attempting to get profile for userId={}", userId);
        ProfileEntity profileEntity = findProfileByUserId(userId);
        return toDTO(profileEntity);
    }
}

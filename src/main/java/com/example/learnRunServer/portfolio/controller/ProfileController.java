package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.ProfileDTO;
import com.example.learnRunServer.portfolio.Service.ProfileService;
import com.example.learnRunServer.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;


    // 프로필 추가 컨트롤러
    @PostMapping("/profile/save")
    public ResponseEntity<Void> saveProfile(ProfileDTO profileDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        profileService.saveProfile(profileDTO, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 프로필 수정 컨트롤러
    @PutMapping("/profile/update")
    public ResponseEntity<Void> updateProfile(ProfileDTO profileDTO) {
        profileService.updateProfile(profileDTO);
        return ResponseEntity.ok().build();
    }

    // 프로필 삭제 컨트롤러
    @DeleteMapping("/profile/delete")
    public ResponseEntity<Void> deleteProfile(ProfileDTO profileDTO) {
        profileService.deleteProfile(profileDTO);
        return ResponseEntity.ok().build();
    }

    // 프로필 불러오기 컨트롤러
    @GetMapping("/profile/get")
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ProfileDTO profileDTO = profileService.getProfile(customUserDetails.getUserId());
        return ResponseEntity.ok().body(profileDTO);
    }
}

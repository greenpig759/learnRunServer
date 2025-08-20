package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.ProfileDTO;
import com.example.learnRunServer.portfolio.Service.ProfileService;
import com.example.learnRunServer.token.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Tag(name = "Profile API", description = "프로필 관련 API 모음")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "프로필 등록", description = "새로운 프로필 등록, 생성된 프로필 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로필 등록 성공")
    })
    @PostMapping("/profile/save")
    public ResponseEntity<Long> saveProfile(@Valid @RequestBody ProfileDTO profileDTO,
                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to save profile: {} for userId={}", profileDTO, customUserDetails.getUserId());
        Long profileId = profileService.saveProfile(profileDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(profileId)
                .toUri();

        return ResponseEntity.created(location).body(profileId);
    }

    @Operation(summary = "프로필 수정", description = "기존 프로필 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공")
    })
    @PutMapping("/profile/update")
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody ProfileDTO profileDTO,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to update profile for userId={}: {}", customUserDetails.getUserId(), profileDTO);
        profileService.updateProfile(profileDTO, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 삭제", description = "기존 프로필 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 삭제 성공")
    })
    @DeleteMapping("/profile/delete")
    public ResponseEntity<Void> deleteProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to delete profile for userId={}", customUserDetails.getUserId());
        profileService.deleteProfile(customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로필 조회", description = "사용자의 프로필 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    })
    @GetMapping("/profile/get")
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to get profile for userId={}", customUserDetails.getUserId());
        ProfileDTO profileDTO = profileService.getProfile(customUserDetails.getUserId());
        return ResponseEntity.ok(profileDTO);
    }
}

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/learnRun/profile")
@RequiredArgsConstructor
@Tag(name = "Profile API", description = "프로필 관련 API 모음")
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "프로필 등록", description = "새로운 프로필 등록. 사용자당 하나의 프로필만 생성 가능.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로필 등록 성공")
    })
    @PostMapping
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
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @PutMapping
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileDTO profileDTO,
                                           @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to update profile for userId={}: {}", customUserDetails.getUserId(), profileDTO);
        try {
            profileService.updateProfile(profileDTO, customUserDetails.getUserId());
            return ResponseEntity.ok().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "프로필 삭제", description = "기존 프로필 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "프로필 삭제 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @DeleteMapping
    public ResponseEntity<?> deleteProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to delete profile for userId={}", customUserDetails.getUserId());
        try {
            profileService.deleteProfile(customUserDetails.getUserId());
            return ResponseEntity.noContent().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "프로필 조회", description = "사용자의 프로필 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.debug("Request to get profile for userId={}", customUserDetails.getUserId());
        ProfileDTO profileDTO = profileService.getProfile(customUserDetails.getUserId());
        return ResponseEntity.ok(profileDTO);
    }
}

package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.WorkExperienceDTO;
import com.example.learnRunServer.portfolio.Service.WorkExperienceService;
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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Tag(name = "WorkExperience API", description = "경력 관련 API 모음")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    @Operation(summary = "경력 등록", description = "새로운 경력 등록, 생성된 경력 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "경력 등록 성공")
    })
    @PostMapping("/workExperience/save")
    public ResponseEntity<Long> saveWorkExperience(@Valid @RequestBody WorkExperienceDTO workExperienceDTO,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to save work experience: {} for userId={}", workExperienceDTO, customUserDetails.getUserId());
        Long workExperienceId = workExperienceService.saveWorkExperience(workExperienceDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(workExperienceId)
                .toUri();

        return ResponseEntity.created(location).body(workExperienceId);
    }

    @Operation(summary = "경력 수정", description = "기존 경력 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "경력 수정 성공")
    })
    @PutMapping("/workExperience/update/{workExperienceId}")
    public ResponseEntity<Void> updateWorkExperience(@PathVariable Long workExperienceId,
                                                     @Valid @RequestBody WorkExperienceDTO workExperienceDTO){
        log.debug("Request to update workExperienceId={}: {}", workExperienceId, workExperienceDTO);
        workExperienceService.updateWorkExperience(workExperienceId, workExperienceDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "경력 삭제", description = "기존 경력 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "경력 삭제 성공")
    })
    @DeleteMapping("/workExperience/delete/{workExperienceId}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long workExperienceId){
        log.debug("Request to delete workExperienceId={}", workExperienceId);
        workExperienceService.deleteWorkExperience(workExperienceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "경력 조회", description = "사용자의 모든 경력 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "경력 조회 성공")
    })
    @GetMapping("/workExperience/get")
    public ResponseEntity<List<WorkExperienceDTO>> getWorkExperiences(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get work experiences for userId={}", customUserDetails.getUserId());
        List<WorkExperienceDTO> workExperienceDTOList = workExperienceService.getWorkExperiences(customUserDetails.getUserId());
        return ResponseEntity.ok(workExperienceDTOList);
    }
}

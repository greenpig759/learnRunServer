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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/learnRun/work-experiences")
@RequiredArgsConstructor
@Tag(name = "WorkExperience API", description = "경력 관련 API 모음")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    @Operation(summary = "경력 등록", description = "새로운 경력 등록, 생성된 경력 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "경력 등록 성공")
    })
    @PostMapping
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
            @ApiResponse(responseCode = "200", description = "경력 수정 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @PutMapping("/{workExperienceId}")
    public ResponseEntity<?> updateWorkExperience(@PathVariable Long workExperienceId,
                                                  @Valid @RequestBody WorkExperienceDTO workExperienceDTO,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to update workExperienceId={}: {}", workExperienceId, workExperienceDTO);
        try {
            workExperienceService.updateWorkExperience(workExperienceId, workExperienceDTO, customUserDetails.getUserId());
            return ResponseEntity.ok().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "경력 삭제", description = "기존 경력 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "경력 삭제 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @DeleteMapping("/{workExperienceId}")
    public ResponseEntity<?> deleteWorkExperience(@PathVariable Long workExperienceId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to delete workExperienceId={}", workExperienceId);
        try {
            workExperienceService.deleteWorkExperience(workExperienceId, customUserDetails.getUserId());
            return ResponseEntity.noContent().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "경력 전체 조회", description = "사용자의 모든 경력 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "경력 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<WorkExperienceDTO>> getWorkExperiences(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get work experiences for userId={}", customUserDetails.getUserId());
        List<WorkExperienceDTO> workExperienceDTOList = workExperienceService.getWorkExperiences(customUserDetails.getUserId());
        return ResponseEntity.ok(workExperienceDTOList);
    }
}

package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.EducationDTO;
import com.example.learnRunServer.portfolio.Service.EducationService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/learnRun/educations")
@RequiredArgsConstructor
@Tag(name = "Education API", description = "학력 관련 API 모음")
public class EducationController {
    private final EducationService educationService;

    @Operation(summary = "학력 등록", description = "새로운 학력 등록, 생성된 학력 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "학력 등록 성공")
    })
    @PostMapping
    public ResponseEntity<Long> saveEducation(@Valid @RequestBody EducationDTO educationDTO,
                                              @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to save education: {} for userId={}", educationDTO, customUserDetails.getUserId());
        Long educationId = educationService.saveEducation(educationDTO, customUserDetails.getUserId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(educationId)
                .toUri();
        return ResponseEntity.created(location).body(educationId);
    }

    @Operation(summary = "학력 수정", description = "기존 학력 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "학력 수정 성공")
    })
    @PutMapping("/{educationId}")
    public ResponseEntity<Void> updateEducation(@PathVariable Long educationId,
                                             @Valid @RequestBody EducationDTO educationDTO,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to update educationId={}: {}", educationId, educationDTO);
        educationService.updateEducation(educationId, educationDTO, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학력 삭제", description = "기존 학력 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "학력 삭제 성공")
    })
    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to delete educationId={}", educationId);
        educationService.deleteEducation(educationId, customUserDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "학력 전체 조회", description = "사용자의 모든 학력 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "학력 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<EducationDTO>> getEducations(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get educations for userId={}", customUserDetails.getUserId());
        List<EducationDTO> educationDTOList = educationService.getEducations(customUserDetails.getUserId());
        return ResponseEntity.ok(educationDTOList);
    }
}

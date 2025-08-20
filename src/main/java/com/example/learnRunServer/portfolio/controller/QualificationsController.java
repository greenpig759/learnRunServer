package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.QualificationsDTO;
import com.example.learnRunServer.portfolio.Service.QualificationsService;
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
@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Tag(name = "Qualifications API", description = "자격증 관련 API 모음")
public class QualificationsController {
    private final QualificationsService qualificationsService;

    @Operation(summary = "자격증 등록", description = "새로운 자격증 등록, 생성된 자격증 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "자격증 등록 성공")
    })
    @PostMapping("/qualifications/save")
    public ResponseEntity<Long> saveQualifications(@Valid @RequestBody QualificationsDTO qualificationsDTO,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to save qualification: {} for userId={}", qualificationsDTO, customUserDetails.getUserId());
        Long qualificationsId = qualificationsService.saveQualifications(qualificationsDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(qualificationsId)
                .toUri();

        return ResponseEntity.created(location).body(qualificationsId);
    }

    @Operation(summary = "자격증 수정", description = "기존 자격증 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자격증 수정 성공")
    })
    @PutMapping("/qualifications/update/{qualificationsId}")
    public ResponseEntity<Void> updateQualifications(@PathVariable Long qualificationsId,
                                                     @Valid @RequestBody QualificationsDTO qualificationsDTO){
        log.debug("Request to update qualificationId={}: {}", qualificationsId, qualificationsDTO);
        qualificationsService.updateQualifications(qualificationsId, qualificationsDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "자격증 삭제", description = "기존 자격증 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자격증 삭제 성공")
    })
    @DeleteMapping("/qualifications/delete/{qualificationsId}")
    public ResponseEntity<Void> deleteQualifications(@PathVariable Long qualificationsId){
        log.debug("Request to delete qualificationId={}", qualificationsId);
        qualificationsService.deleteQualifications(qualificationsId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "자격증 전체 조회", description = "사용자의 모든 자격증 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자격증 조회 성공")
    })
    @GetMapping("/qualifications/get")
    public ResponseEntity<List<QualificationsDTO>> getQualifications(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get qualifications for userId={}", customUserDetails.getUserId());
        List<QualificationsDTO> qualificationsDTOList = qualificationsService.getQualifications(customUserDetails.getUserId());
        return ResponseEntity.ok(qualificationsDTOList);
    }
}

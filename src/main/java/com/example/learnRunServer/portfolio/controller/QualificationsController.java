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
@RequestMapping("/learnRun/qualifications")
@RequiredArgsConstructor
@Tag(name = "Qualifications API", description = "자격증 관련 API 모음")
public class QualificationsController {
    private final QualificationsService qualificationsService;

    @Operation(summary = "자격증 등록", description = "새로운 자격증 등록, 생성된 자격증 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "자격증 등록 성공")
    })
    @PostMapping
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
            @ApiResponse(responseCode = "200", description = "자격증 수정 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @PutMapping("/{qualificationsId}")
    public ResponseEntity<?> updateQualifications(@PathVariable Long qualificationsId,
                                                  @Valid @RequestBody QualificationsDTO qualificationsDTO,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to update qualificationId={}: {}", qualificationsId, qualificationsDTO);
        try {
            qualificationsService.updateQualifications(qualificationsId, qualificationsDTO, customUserDetails.getUserId());
            return ResponseEntity.ok().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "자격증 삭제", description = "기존 자격증 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "자격증 삭제 성공"),
            @ApiResponse(responseCode = "409", description = "데이터 충돌 발생")
    })
    @DeleteMapping("/{qualificationsId}")
    public ResponseEntity<?> deleteQualifications(@PathVariable Long qualificationsId,
                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to delete qualificationId={}", qualificationsId);
        try {
            qualificationsService.deleteQualifications(qualificationsId, customUserDetails.getUserId());
            return ResponseEntity.noContent().build();
        } catch (ObjectOptimisticLockingFailureException e) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", 409);
            responseBody.put("error", "Conflict");
            responseBody.put("message", "데이터가 접근에 의해 변경되었습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "자격증 전체 조회", description = "사용자의 모든 자격증 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자격증 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<QualificationsDTO>> getQualifications(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get qualifications for userId={}", customUserDetails.getUserId());
        List<QualificationsDTO> qualificationsDTOList = qualificationsService.getQualifications(customUserDetails.getUserId());
        return ResponseEntity.ok(qualificationsDTOList);
    }
}

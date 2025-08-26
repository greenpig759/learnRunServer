package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Service.AwardService;
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
@RequestMapping("/learnRun/awards")
@RequiredArgsConstructor
@Tag(name = "Award API", description = "수상경력 관련 API 모음")
public class AwardController {
    private final AwardService awardService;

    // 수상경력 등록 컨트롤러
    @Operation(summary = "수상경력 등록", description = "새로운 수상경력 등록, 생성된 수상경력 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "수상경력 등록 성공")
    })
    @PostMapping
    public ResponseEntity<Long> saveAward(@Valid @RequestBody AwardDTO awardDTO,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to save award: {} for userId={}", awardDTO, customUserDetails.getUserId());
        Long awardId = awardService.saveAward(awardDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(awardId)
                .toUri();

        return ResponseEntity.created(location).body(awardId);
    }

    // 수상경력 수정 컨트롤러
    @Operation(summary = "수상경력 수정", description = "기존 수상경력 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 수정 성공"),
    })
    @PutMapping("/{awardId}")
    public ResponseEntity<Void> updateAward(@PathVariable Long awardId,
                                         @Valid @RequestBody AwardDTO awardDTO,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to update awardId={}: {}", awardId, awardDTO);
        return ResponseEntity.ok().build();
    }

    // 수상경력 삭제 컨트롤러
    @Operation(summary = "수상경력 삭제", description = "기존 수상경력 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수상경력 삭제 성공"),
    })
    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to delete awardId={}", awardId);
        return ResponseEntity.ok().build();
    }

    // 수상경력 조회 컨트롤러
    @Operation(summary = "수상경력 전체 조회", description = "사용자의 모든 수상경력 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<AwardDTO>> getAwards(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get awards for userId={}", customUserDetails.getUserId());
        List<AwardDTO> awardDTOList = awardService.getAwards(customUserDetails.getUserId());
        return ResponseEntity.ok(awardDTOList);
    }
}

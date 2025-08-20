package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Service.AwardService;
import com.example.learnRunServer.token.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Tag(name = "Award API", description = "수상경력 관련 API 모음")
public class AwardController {
    private final AwardService awardService;

    @Operation(summary = "수상경력 등록", description = "새로운 수상경력 등록, 생성된 수상경력 ID 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "수상경력 등록 성공")
    })
    @PostMapping("/award/save")
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

    @Operation(summary = "수상경력 수정", description = "기존 수상경력 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 수정 성공")
    })
    @PutMapping("/award/update/{awardId}")
    public ResponseEntity<Void> updateAward(@PathVariable Long awardId, @Valid @RequestBody AwardDTO awardDTO){
        log.debug("Request to update awardId={}: {}", awardId, awardDTO);
        awardService.updateAward(awardId, awardDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수상경력 삭제", description = "기존 수상경력 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 삭제 성공")
    })
    @DeleteMapping("/award/delete/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId){
        log.debug("Request to delete awardId={}", awardId);
        awardService.deleteAward(awardId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수상경력 조회", description = "사용자의 모든 수상경력 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 조회 성공")
    })
    @GetMapping("/award/get")
    public ResponseEntity<List<AwardDTO>> getAwards(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.debug("Request to get awards for userId={}", customUserDetails.getUserId());
        List<AwardDTO> awardDTOList = awardService.getAwards(customUserDetails.getUserId());
        return ResponseEntity.ok(awardDTOList);
    }
}

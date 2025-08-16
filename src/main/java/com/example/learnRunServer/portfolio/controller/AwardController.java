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

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Tag(name = "수상경력 API", description = "수상경력 생성, 수정, 삭제, 조회 API입니다.")
public class AwardController {
    private final AwardService awardService;

    @Operation(summary = "수상경력 추가", description = "사용자의 수상경력을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/award/save")
    public ResponseEntity<Void> saveAward(@Valid @RequestBody AwardDTO awardDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        awardService.saveAward(awardDTO, customUserDetails.getUserId());
        log.info("받은 수상경력 정보: {}", awardDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수상경력 수정", description = "지정한 ID의 수상경력을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "수상경력 미존재")
    })
    @PutMapping("/award/update/{awardId}")
    public ResponseEntity<Void> updateAward(@PathVariable Long awardId, @Valid @RequestBody AwardDTO awardDTO){
        awardService.updateAward(awardId, awardDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수상경력 삭제", description = "지정한 ID의 수상경력을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "수상경력 미존재")
    })
    @DeleteMapping("/award/delete/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId){
        awardService.deleteAward(awardId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수상경력 조회", description = "사용자의 모든 수상경력을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수상경력 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/award/get")
    public ResponseEntity<List<AwardDTO>> getAwards(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<AwardDTO> awardDTOS = awardService.getAwards(customUserDetails.getUserId());
        return ResponseEntity.ok(awardDTOS);
    }
}

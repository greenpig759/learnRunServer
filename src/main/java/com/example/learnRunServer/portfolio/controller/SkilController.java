package com.example.learnRunServer.portfolio.controller;


import com.example.learnRunServer.portfolio.DTO.SkilDTO;
import com.example.learnRunServer.portfolio.Service.SkilService;
import com.example.learnRunServer.token.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("learnRun")
@RequiredArgsConstructor
@Tag(name = "Skil API", description = "스킬 관련 API 모음")

public class SkilController {
    private final SkilService skilService;

    // 스킬 등록 메서드
    @PostMapping("/skil")
    @Operation(summary = "스킬등록", description = "새로운 스킬을 등록, 생성된 스킬 ID를 반납")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "스킬 등록 성공")
    })
    public ResponseEntity<Long> saveSkil(@Valid @RequestBody SkilDTO skilDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long skilId = skilService.saveSkil(skilDTO, customUserDetails.getUserId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(skilId)
                .toUri();
        return ResponseEntity.created(location).body(skilId);
    }

    // 스킬 수정 메서드
    @PutMapping("/skil/{skilId}")
    @Operation(summary = "스킬 수정", description = "기존의 스킬을 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    public ResponseEntity<Void> updateSkil(@PathVariable Long skilId, @Valid @RequestBody SkilDTO skilDTO) {
        skilService.updateSkil(skilId, skilDTO);
        return ResponseEntity.ok().build();
    }

    // 스킬 삭제 메서드
    @DeleteMapping("/skil/{skilId}")
    @Operation(summary = "스킬 삭제", description = "지정한 스킬을 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    public ResponseEntity<Void> deleteSkil(@PathVariable Long skilId) {
        skilService.deleteSkil(skilId);
        return ResponseEntity.ok().build();
    }

    // 스킬 전부 가져오기 메서드
    @GetMapping("/skil")
    @Operation(summary = "스킬 전체 조회", description = "사용자의 모든 스킬 목록을 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public  ResponseEntity<List<SkilDTO>> findAllSkils(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<SkilDTO> skilDTOList = skilService.findAllSkils(customUserDetails.getUserId());
        return ResponseEntity.ok().body(skilDTOList);
    }

}

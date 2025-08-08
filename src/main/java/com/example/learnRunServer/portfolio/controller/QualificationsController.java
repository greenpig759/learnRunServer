package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.QualificationsDTO;
import com.example.learnRunServer.portfolio.Service.QualificationsService;
import com.example.learnRunServer.token.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
public class QualificationsController {
    private final QualificationsService qualificationsService;

    // 자격증 추가 컨트롤러
    @PostMapping("/qualifications/save")
    public ResponseEntity<Void> saveQualifications(@RequestBody QualificationsDTO qualificationsDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        qualificationsService.saveQualifications(qualificationsDTO, customUserDetails.getUserId());
        log.info("학력 정보: {}",qualificationsDTO); // 로그를 낋여봤습니다..
        return ResponseEntity.ok().build();
    }

    // 자격증 수정 컨트롤러
    @PutMapping("/qualifications/update/{qualificationsId}")
    public ResponseEntity<Void> updateQualifications(@PathVariable Long qualificationsId, @RequestBody QualificationsDTO qualificationsDTO){
        qualificationsService.updateQualifications(qualificationsId, qualificationsDTO);

        return ResponseEntity.ok().build();
    }

    // 자격증 삭제 컨트롤러
    @DeleteMapping("/qualifications/delete/{qualificationsId}")
    public ResponseEntity<Void> deleteQualifications(@PathVariable Long qualificationsId){
        qualificationsService.deleteQualifications(qualificationsId);

        return ResponseEntity.ok().build();
    }

    // 자격증 불러오기 컨트롤러
    @GetMapping("/qualifications/get")
    public ResponseEntity<List<QualificationsDTO>> getQualifications(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<QualificationsDTO> qualificationsDTOS = qualificationsService.getQualifications(customUserDetails.getUserId());
        return ResponseEntity.ok(qualificationsDTOS);
    }
}

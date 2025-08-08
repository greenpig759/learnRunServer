package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.WorkExperienceDTO;
import com.example.learnRunServer.portfolio.Service.WorkExperienceService;
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
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    // 경력 추가 컨트롤러
    @PostMapping("/workExperience/save")
    public ResponseEntity<Void> saveWorkExperience(@RequestBody WorkExperienceDTO workExperienceDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        workExperienceService.saveWorkExperience(workExperienceDTO, customUserDetails.getUserId());
        log.info("경력 저장 정보: {}",workExperienceDTO);
        return ResponseEntity.ok().build();
    }

    // 경력 수정 컨트롤러
    @PutMapping("/workExperience/update/{workExperienceId}")
    public ResponseEntity<Void> updateWorkExperience(@PathVariable Long workExperienceId, @RequestBody WorkExperienceDTO workExperienceDTO){
        workExperienceService.updateWorkExperience(workExperienceId, workExperienceDTO);
        return ResponseEntity.ok().build();
    }

    // 경력 삭제 컨트롤러
    @DeleteMapping("/workExperience/delete/{workExperienceId}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long workExperienceId){
        workExperienceService.deleteWorkExperience(workExperienceId);
        return ResponseEntity.ok().build();
    }

    // 경력 리스트 불러오기 컨트롤러
    @GetMapping("/workExperience/get")
    public ResponseEntity<List<WorkExperienceDTO>> getWorkExperience(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<WorkExperienceDTO> workExperienceDTOS = workExperienceService.getWorkExperience(customUserDetails.getUserId());
        return ResponseEntity.ok(workExperienceDTOS);

    }
}

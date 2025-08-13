package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.EducationDTO;
import com.example.learnRunServer.portfolio.Service.EducationService;
import com.example.learnRunServer.token.CustomUserDetails;
import jakarta.validation.Valid;
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
public class EducationController {
    private final EducationService educationService;
    // 학력 추가 컨트롤러
    @PostMapping("/education/save")
    public ResponseEntity<Void> saveEducation(@Valid @RequestBody EducationDTO educationDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        educationService.saveEducation(educationDTO, customUserDetails.getUserId());
        log.info("학력 정보: {}",educationDTO); // 로그를 낋여봤습니다..
        return ResponseEntity.ok().build();
    }

    // 학력 수정 컨트롤러
    @PutMapping("/education/update/{educationId}")
    public ResponseEntity<Void> updateEducation(@PathVariable Long educationId, @Valid @RequestBody EducationDTO educationDTO){
        educationService.updateEducation(educationId, educationDTO);
        return ResponseEntity.ok().build();
    }

    // 학력 삭제 컨트롤러
    @DeleteMapping("/education/delete/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId){
        educationService.deleteEducation(educationId);
        return ResponseEntity.ok().build();
    }

    // 학력 불러오기 컨트롤러
    @GetMapping("/education/get")
    public ResponseEntity<List<EducationDTO>> getEducation(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<EducationDTO> educationDTOS = educationService.getEducations(customUserDetails.getUserId());
        return ResponseEntity.ok(educationDTOS);
    }
}

package com.example.learnRunServer.portfolio.controller;

import com.example.learnRunServer.portfolio.DTO.AwardDTO;
import com.example.learnRunServer.portfolio.Service.AwardService;
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
public class AwardController {
    private final AwardService awardService;

    // 수상경력 추가 컨트롤러
    @PostMapping("/award/save")
    public ResponseEntity<Void> saveAward(@RequestBody AwardDTO awardDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        awardService.saveAward(awardDTO, customUserDetails.getUserId());
        log.info("받은 수상경력 정보: {}",awardDTO);
        return ResponseEntity.ok().build();
    }

    // 수상경력 수정 컨트롤러
    @PutMapping("/award/update")
    public ResponseEntity<Void> updateAward(@RequestBody AwardDTO awardDTO){
        awardService.updateAward(awardDTO);
        return ResponseEntity.ok().build();
    }

    // 수상경력 삭제 컨트롤러
    @DeleteMapping("/award/delete")
    public ResponseEntity<Void> deleteAward(@RequestBody AwardDTO awardDTO){
        awardService.deleteAward(awardDTO);
        return ResponseEntity.ok().build();
    }

    // 수상경력 불러오기 컨트롤러
    @GetMapping("/award/get")
    public ResponseEntity<List<AwardDTO>> getAwards(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<AwardDTO> awardDTOS = awardService.getAwards(customUserDetails.getUserId());
        return ResponseEntity.ok(awardDTOS);
    }
}

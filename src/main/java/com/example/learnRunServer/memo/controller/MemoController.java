package com.example.learnRunServer.memo.controller;

import com.example.learnRunServer.memo.DTO.MemoDTO;
import com.example.learnRunServer.memo.Entity.MemoEntity;
import com.example.learnRunServer.memo.repository.MemoRepository;
import com.example.learnRunServer.memo.service.MemoService;
import com.example.learnRunServer.token.CustomUserDetails;
import com.example.learnRunServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/learnRun")
@RequiredArgsConstructor
@Controller
public class MemoController {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;
    private final MemoService memoService;

    // 메모 저장 컨트롤러
    @PostMapping("/memo/save")
    public ResponseEntity<Void> saveMemo(@RequestBody MemoDTO memoDTO, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        memoService.saveMemo(memoDTO, customUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 메모 수정 컨트롤러
    @PutMapping("/memo/update")
    public ResponseEntity<Void> updateMemo(@RequestBody MemoDTO memoDTO) {
        memoService.updateMemo(memoDTO);
        return ResponseEntity.ok().build();
    }

    // 메모 삭제 컨트롤러
    @DeleteMapping("/memo/delete")
    public ResponseEntity<Void> deleteMemo(@RequestBody MemoDTO memoDTO) {
        memoService.deleteMemo(memoDTO);
        return ResponseEntity.ok().build();
    }

    // 작성글 불러오기(전부) 컨트롤러
    @GetMapping("/memo/all")
    public ResponseEntity<List<MemoDTO>> findAllMemo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<MemoDTO> memoDTOs = memoService.getAllMemos(customUserDetails.getUserId());

        return ResponseEntity.ok(memoDTOs);
    }

}

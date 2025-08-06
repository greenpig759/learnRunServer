package com.example.learnRunServer.user.controller;


import com.example.learnRunServer.user.DTO.UserDTO;
import com.example.learnRunServer.user.repository.UserRepository;
import com.example.learnRunServer.user.service.UserService;
import com.example.learnRunServer.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/learnRun")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;


    // 로그인 컨트롤러
    @PostMapping("/user/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserDTO userDTO){
        TokenResponse tokenResponse = userService.login(userDTO);
        return ResponseEntity.ok(tokenResponse);
    }
}

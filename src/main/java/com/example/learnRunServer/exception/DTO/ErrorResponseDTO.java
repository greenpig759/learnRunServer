package com.example.learnRunServer.exception.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO {
    private int status;
    private String code; // 커스텀 에러코드로 필요에 따라 null 가능
    private String message;

    // 생성자 1: 단순 메시지만 보내는 경우
    public ErrorResponseDTO(int status, String message){
        this.status = status;
        this.message = message;
    }

    // 생성자 2: 커스텀 에러 메세지를 포함하는 경우
    public ErrorResponseDTO(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

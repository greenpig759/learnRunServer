package com.example.learnRunServer.portfolio.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardDTO {
    private Long awardId; // pk

    @NotBlank(message = "제목을 입력해주세요.") // DTO 유효성 검증. 실패 시 메시지
    private String title;

    @NotNull(message = "날짜를 입력해주세요.")
    @PastOrPresent(message = "날짜는 과거 또는 현재여야 합니다.")
    private LocalDate date;

    // 낙관적 락: 버전 검증 후 수정/삭제
    private Long version;
}

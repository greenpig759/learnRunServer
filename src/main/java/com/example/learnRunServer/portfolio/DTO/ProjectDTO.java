package com.example.learnRunServer.portfolio.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long Id;

    @NotNull(message = "시작일을 입력해주세요.")
    @PastOrPresent(message = "시작일은 과거 또는 현재여야 합니다.")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "프로젝트 이름을 입력해주세요.")
    private String title;

    private String text;
}

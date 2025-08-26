package com.example.learnRunServer.portfolio.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QualificationsDTO {
    private Long qualificationsId;

    @NotBlank(message = "자격증 이름을 입력해주세요.")
    private String title;

    @NotNull(message = "취득일을 입력해주세요.")
    @PastOrPresent(message = "취득일은 과거 또는 현재여야 합니다.")
    private LocalDate date;
}

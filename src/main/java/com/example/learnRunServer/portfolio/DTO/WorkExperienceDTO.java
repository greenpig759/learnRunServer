package com.example.learnRunServer.portfolio.DTO;

import com.example.learnRunServer.portfolio.DTO.validation.DateRange;
import com.example.learnRunServer.portfolio.DTO.validation.StartEndDate;
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
@StartEndDate
public class WorkExperienceDTO implements DateRange {
    private Long workExperienceId;

    @NotBlank(message = "경력명을 입력해주세요.")
    private String title;

    @NotNull(message = "시작일을 입력해주세요.")
    @PastOrPresent(message = "시작일은 과거 또는 현재여야 합니다.")
    private LocalDate startDate;

    @PastOrPresent(message = "종료일은 과거 또는 현재여야 합니다.")
    private LocalDate endDate;
}

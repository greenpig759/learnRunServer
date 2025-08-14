package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class WorkExperienceDTO {
    private Long workExperienceId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}

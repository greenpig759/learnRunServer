package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationDTO {
    private Long educationId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
}

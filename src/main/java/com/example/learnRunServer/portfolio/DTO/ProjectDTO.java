package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long Id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String text;
}

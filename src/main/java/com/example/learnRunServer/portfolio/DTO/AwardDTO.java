package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardDTO {
    private Long awardId;
    private String title;
    private LocalDate date;
}

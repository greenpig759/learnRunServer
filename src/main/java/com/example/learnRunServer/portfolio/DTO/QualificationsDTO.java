package com.example.learnRunServer.portfolio.DTO;

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
    private String title;
    private LocalDate qualificationsDate;
}

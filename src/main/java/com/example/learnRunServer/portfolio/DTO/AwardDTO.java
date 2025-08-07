package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwardDTO {
    private Long awardId;
    private String title;
    private String date;
}

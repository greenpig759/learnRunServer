package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkilDTO {
    private Long skilId;
    private String title;
    private String text;
}

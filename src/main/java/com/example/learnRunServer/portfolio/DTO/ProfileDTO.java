package com.example.learnRunServer.portfolio.DTO;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private Long profileId;
    private String name;
    private String email;
}

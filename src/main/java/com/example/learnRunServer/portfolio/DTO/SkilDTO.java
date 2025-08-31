package com.example.learnRunServer.portfolio.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkilDTO {
    private Long Id;

    @NotBlank(message = "스킬 이름을 입력해주세요.")
    private String title;

    @NotBlank(message = "스킬 내용을 입력해주세요.")
    private String text;
}

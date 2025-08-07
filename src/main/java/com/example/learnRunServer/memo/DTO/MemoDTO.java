package com.example.learnRunServer.memo.DTO;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDTO {
    private Long memoId;
    private String memoTitle;
    private String memoContent;
    private Long user;
}

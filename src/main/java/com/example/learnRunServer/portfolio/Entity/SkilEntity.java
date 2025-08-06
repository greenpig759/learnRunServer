package com.example.learnRunServer.portfolio.Entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "skil_table")
public class SkilEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skilId;

    // 성빈이랑 합의 보기
}

package com.example.learnRunServer.user.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // pk

    @Column(nullable = false) // 카카오톡 로그인을 통해 받는 유저의 고유 id
    private String kakaoId;
}
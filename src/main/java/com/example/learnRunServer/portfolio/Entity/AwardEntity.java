package com.example.learnRunServer.portfolio.Entity;


import com.example.learnRunServer.user.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString(exclude = "user") // 불필요한 쿼리 추가 차단
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "award_table")
public class AwardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
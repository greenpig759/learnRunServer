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
@Table(name = "award_table")
public class AwardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long awardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String date;
}

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
@Table(name = "education_table")
public class EducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;
}

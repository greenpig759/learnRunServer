package com.example.learnRunServer.portfolio.Entity;

import com.example.learnRunServer.user.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "qualifications_table")
public class QualificationsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long qualificationsId;

    @Column(nullable = false)
    private String title;

    // 수상경력은 String으로 하고 자격증은 LocalDate로 한 이유?
    @Column(nullable = false)
    private LocalDate qualificationsDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    // 왜 start랑 end로 했는지 확인해보기
}

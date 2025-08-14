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

    @Column(nullable = false)
    private String skilTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skil_detail_id")
    private SkilDetailEntity skilDetail;
}

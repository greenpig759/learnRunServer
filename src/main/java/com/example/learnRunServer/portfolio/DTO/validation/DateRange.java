package com.example.learnRunServer.portfolio.DTO.validation;


import java.time.LocalDate;

// 시작일과 종료일을 가지는 DTO 객체들을 연결
public interface DateRange {
    LocalDate getStartDate();
    LocalDate getEndDate();
}
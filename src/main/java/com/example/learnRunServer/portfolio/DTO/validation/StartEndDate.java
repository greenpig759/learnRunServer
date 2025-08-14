package com.example.learnRunServer.portfolio.DTO.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// @StartEndDate 어노테이션 작성
@Documented
@Constraint(validatedBy = StartEndDateValidator.class) // 어떤 Validator를 사용할지 지정
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE}) // 클래스 레벨에 적용
@Retention(RetentionPolicy.RUNTIME)
public @interface StartEndDate {
    String message() default "시작일은 종료일보다 늦을 수 없습니다.";
    Class<?>[] groups() default {}; // 유효성 검사 groups
    Class<? extends Payload>[] payload() default {}; // 유효성 검사 Payload
}
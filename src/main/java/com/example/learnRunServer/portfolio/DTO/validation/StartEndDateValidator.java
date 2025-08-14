package com.example.learnRunServer.portfolio.DTO.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class StartEndDateValidator implements ConstraintValidator<StartEndDate, DateRange> {

    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
        if (dateRange == null) return true;

        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate   = dateRange.getEndDate();

        // 개별 필드 @NotNull 등이 처리하도록 여기서는 통과
        if (startDate == null || endDate == null) return true;

        if (startDate.isAfter(endDate)) {
            // 기본(ObjectError) 생성 막고 특정 필드에 귀속
            context.disableDefaultConstraintViolation();

            // @StartEndDate.message() 값을 그대로 사용
            String message = context.getDefaultConstraintMessageTemplate();

            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("Duration") // 필드 이름
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
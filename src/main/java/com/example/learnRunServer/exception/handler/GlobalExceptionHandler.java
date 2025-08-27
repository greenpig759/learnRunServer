package com.example.learnRunServer.exception.handler;

import com.example.learnRunServer.exception.AwardNotFoundException;
import com.example.learnRunServer.exception.DTO.ErrorResponseDTO;
import com.example.learnRunServer.exception.EducationNotFoundException;
import com.example.learnRunServer.exception.ProfileNotFoundException;
import com.example.learnRunServer.exception.QualificationsNotFoundException;
import com.example.learnRunServer.exception.UserNotFoundException;
import com.example.learnRunServer.exception.WorkExperienceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // AwardNotFoundException 예외 처리 핸들러
    @ExceptionHandler(AwardNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAwardNotFound(AwardNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // EducationNotFoundException 예외 처리 핸들러
    @ExceptionHandler(EducationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEducationNotFound(EducationNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ProfileNotFoundException 예외 처리 핸들러
    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProfileNotFound(ProfileNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // QualificationsNotFoundException 예외 처리 핸들러
    @ExceptionHandler(QualificationsNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleQualificationsNotFound(QualificationsNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // WorkExperienceNotFoundException 예외 처리 핸들러
    @ExceptionHandler(WorkExperienceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleWorkExperienceNotFound(WorkExperienceNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

package com.survey.exception;

import com.survey.exception.model.CommonErrorResponse;
import com.survey.exception.model.CommonException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponse> handleGenericException(Exception ex) {
        CommonErrorResponse response = new CommonErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonErrorResponse> handleCommonException(CommonException ex) {
        CommonErrorResponse response = new CommonErrorResponse(
                ex.getStatus().value(),
                ex.getError(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return getCommonErrorResponseResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonErrorResponse> handleValidationException(MissingServletRequestParameterException ex) {
        return getCommonErrorResponseResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonErrorResponse> handleValidationException(ConstraintViolationException ex) {
        return getCommonErrorResponseResponseEntity(ex.getMessage());
    }

    private static ResponseEntity<CommonErrorResponse> getCommonErrorResponseResponseEntity(String ex) {
        CommonErrorResponse response = new CommonErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

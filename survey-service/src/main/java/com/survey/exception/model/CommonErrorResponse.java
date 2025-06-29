package com.survey.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommonErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}

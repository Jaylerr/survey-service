package com.survey.model.surveyresponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SurveyAnswer {
    @NotBlank( message = "seq is required")
    private String seq;

    @NotBlank(message = "question is required")
    private String question;

    @NotNull
    private String answer;
}

package com.survey.model.surveyresponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SurveyResponse {
    private Integer respondentId;
    private String seq;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
}

package com.survey.model.surveyresponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SurveyResponseBody {
    @NotNull(message = "respondentId is required")
    private Integer respondentId;

    @NotEmpty(message = "surveyAnswer is required")
    private List<@Valid SurveyAnswer> surveyAnswer;
}
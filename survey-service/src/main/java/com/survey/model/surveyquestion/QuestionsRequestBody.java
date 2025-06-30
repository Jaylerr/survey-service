package com.survey.model.surveyquestion;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionsRequestBody {
    @NotEmpty
    List<SurveyQuestion> questions;
}

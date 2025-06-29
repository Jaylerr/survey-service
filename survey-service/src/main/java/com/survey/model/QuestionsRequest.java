package com.survey.model;

import com.survey.model.surveyquestion.SurveyQuestion;
import lombok.Data;

import java.util.List;

@Data
public class QuestionsRequest {
    List<SurveyQuestion> questions;
}

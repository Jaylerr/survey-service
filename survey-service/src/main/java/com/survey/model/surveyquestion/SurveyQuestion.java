package com.survey.model.surveyquestion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "survey_questions")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SurveyQuestion {
    @Id
    private String seq;
    private String type;
    private String title;
    private List<Setting> settings;
    private List<Choice> choices;
}

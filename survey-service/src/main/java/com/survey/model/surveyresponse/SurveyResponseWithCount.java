package com.survey.model.surveyresponse;

import lombok.Data;

@Data
public class SurveyResponseWithCount {
    private String seq;
    private String question;
    private String answer;
    private Long count;

    public SurveyResponseWithCount(String seq, String question, String answer, Long count) {
        this.seq = seq;
        this.question = question;
        this.answer = answer;
        this.count = count;
    }

    public SurveyResponseWithCount() {
    }
}

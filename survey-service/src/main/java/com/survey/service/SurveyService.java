package com.survey.service;

import com.survey.exception.model.CommonException;
import com.survey.model.QuestionsRequest;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyResponseBody;
import com.survey.model.SurveyResponseSummary;
import com.survey.model.surveyresponse.SurveyResponseEntity;

import java.util.List;

public interface SurveyService {

    List<SurveyQuestion> getSurveyQuestion(String seq) throws CommonException;

    String saveSurveyQuestions(QuestionsRequest request) throws CommonException;

    String saveResponses(SurveyResponseBody surveyResponse) throws CommonException;

    SurveyResponseSummary getSurveyResponseBySeq(String seq) throws CommonException;

    List<SurveyResponseEntity> getSurveyResponseByRespondentId(String respId) throws CommonException;

}

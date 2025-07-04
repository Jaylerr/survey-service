package com.survey.service;

import com.survey.exception.model.CommonException;
import com.survey.model.surveyquestion.QuestionsRequestBody;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.model.surveyresponse.SurveyResponseRequestBody;
import com.survey.model.surveyresponse.SurveyResponseSummary;

import java.util.List;

public interface SurveyService {

    List<SurveyQuestion> getSurveyQuestion(String seq) throws CommonException;

    String saveSurveyQuestions(QuestionsRequestBody request) throws CommonException;

    String saveResponses(SurveyResponseRequestBody surveyResponse) throws CommonException;

    SurveyResponseSummary getSurveyResponseBySeq(String seq) throws CommonException;

    List<SurveyResponseEntity> getSurveyResponseByRespondentId(String respId) throws CommonException;

}

package com.survey.controller;

import com.survey.exception.model.CommonException;
import com.survey.model.surveyquestion.QuestionsRequestBody;
import com.survey.model.surveyresponse.SurveyResponseSummary;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyResponse;
import com.survey.model.surveyresponse.SurveyResponseRequestBody;
import com.survey.service.SurveyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class SurveyControllerTest {

    @InjectMocks
    SurveyController controller;

    @Mock
    SurveyService service;

    @Test
    void addQuestionSuccessTest() throws CommonException {
        QuestionsRequestBody request = new QuestionsRequestBody();
        Mockito.when(service.saveSurveyQuestions(request)).thenReturn("added question success");

        ResponseEntity<String> actual = controller.addNewQuestions(request);

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("added question success", actual.getBody());
    }

    @Test
    void fetchQuestionSuccessTest() throws CommonException {
        SurveyQuestion question = new SurveyQuestion();
        List<SurveyQuestion> questions = List.of(question);
        Mockito.when(service.getSurveyQuestion("0001")).thenReturn(questions);

        ResponseEntity<List<SurveyQuestion>> actual = controller.fetchSurveyQuestion("0001");

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertFalse(actual.getBody().isEmpty());
    }

    @Test
    void submitResponseSuccessTest() throws CommonException {
        SurveyResponseRequestBody response = new SurveyResponseRequestBody();
        response.setSurveyAnswer(new ArrayList<>());
        Mockito.when(service.saveResponses(response)).thenReturn("success");

        ResponseEntity<String> actual = controller.submitResponse(response);

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals("success", actual.getBody());
    }

    @Test
    void getResponseBySeqSuccessTest() throws CommonException {
        SurveyResponseSummary responseBySeq = new SurveyResponseSummary();
        Mockito.when(service.getSurveyResponseBySeq("seq")).thenReturn(responseBySeq);

        ResponseEntity<SurveyResponseSummary> actual = controller.getResponseBySeq("seq");

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(actual.getBody());
    }

    @Test
    void getResponseByRespondentIdSuccessTest() throws CommonException {
        List<SurveyResponse> responseByRespondentId = List.of(new SurveyResponse());
        Mockito.when(service.getSurveyResponseByRespondentId("1112")).thenReturn(responseByRespondentId);

        ResponseEntity<List<SurveyResponse>> actual = controller.getResponseByRespondentId("1112");

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertFalse(actual.getBody().isEmpty());
    }
}

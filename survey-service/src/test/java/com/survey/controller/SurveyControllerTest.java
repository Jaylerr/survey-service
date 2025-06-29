package com.survey.controller;

import com.survey.exception.model.CommonException;
import com.survey.model.SurveyResponseSummary;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyResponseBody;
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
    void fetchQuestionSuccessTest() throws CommonException {
        SurveyQuestion question = new SurveyQuestion();
        List<SurveyQuestion> questions = List.of(question);
        Mockito.when(service.getSurveyQuestion("0001")).thenReturn(questions);

        ResponseEntity<List<SurveyQuestion>> actual = controller.fetchSurveyQuestion("0001");

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(!actual.getBody().isEmpty());
    }

    @Test
    void submitResponseSuccessTest() throws CommonException {
        SurveyResponseBody response = new SurveyResponseBody();
        response.setSurveyAnswer(new ArrayList<>());
        Mockito.when(service.saveResponses(response)).thenReturn("success");

        ResponseEntity<String> actual = controller.submitResponse(response);

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(actual.getBody().equals("success"));
    }

    @Test
    void getResponseSuccessTest() throws CommonException {
        Mockito.when(service.getSurveyResponseBySeq("seq")).thenReturn(null);

        ResponseEntity<SurveyResponseSummary> actual = controller.getResponseBySeq("seq");

        Assertions.assertTrue(actual.getStatusCode().is2xxSuccessful());
    }
}

package com.survey.service;

import com.survey.exception.model.CommonException;
import com.survey.model.QuestionsRequest;
import com.survey.model.SurveyResponseSummary;
import com.survey.model.SurveyResponseWithCount;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyAnswer;
import com.survey.model.surveyresponse.SurveyResponseBody;
import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.repository.jpa.SurveyResponseRepository;
import com.survey.repository.mongo.SurveyQuestionRepository;
import com.mongodb.MongoClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class SurveyServiceImplTest {

    @InjectMocks
    SurveyServiceImpl service;

    @Mock
    SurveyQuestionRepository surveyQuestionRepository;

    @Mock
    SurveyResponseRepository surveyResponseRepository;


    @Test
    void getSurveyQuestionWithSeqSuccessTest() throws CommonException {
        SurveyQuestion survey = new SurveyQuestion();
        Mockito.when(surveyQuestionRepository.findBySeq("0001")).thenReturn(survey);

        List<SurveyQuestion> actual = service.getSurveyQuestion("0001");

        Assertions.assertTrue(actual.size() == 1);
    }

    @Test
    void getSurveyQuestionWithNoSeqSuccessTest() throws CommonException {
        List<SurveyQuestion> surveys = new ArrayList<>();
        surveys.add(new SurveyQuestion());
        surveys.add(new SurveyQuestion());
        Mockito.when(surveyQuestionRepository.findAll()).thenReturn(surveys);

        List<SurveyQuestion> actual = service.getSurveyQuestion("");

        Assertions.assertTrue(actual.size() > 1);
    }

    @Test
    void getSurveyQuestionWithNotExistingSeqShouldReturnEmptyListTest() throws CommonException {
        Mockito.when(surveyQuestionRepository.findBySeq("0001")).thenReturn(null);

        List<SurveyQuestion> actual = service.getSurveyQuestion("0001");

        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void getSurveyQuestionFailShouldThrowCommonErrorTest() {
        Mockito.when(surveyQuestionRepository.findBySeq("0001")).thenThrow(MongoClientException.class);

        Assertions.assertThrows(CommonException.class, () -> service.getSurveyQuestion("0001"));
    }


    @Test
    void saveSurveyQuestionsSuccessTest() throws CommonException {
        SurveyQuestion question = new SurveyQuestion();
        question.setSeq("0001");

        List<SurveyQuestion> questions = new ArrayList<>();
        questions.add(question);

        QuestionsRequest request = new QuestionsRequest();
        request.setQuestions(questions);

        Mockito.when(surveyQuestionRepository.existsBySeq("0001")).thenReturn(true);

        String actual = service.saveSurveyQuestions(request);

        Mockito.verify(surveyQuestionRepository, Mockito.atLeastOnce()).saveAll(any());
        Assertions.assertEquals("added question success", actual);
    }

    @Test
    void saveSurveyQuestionsFailTest() throws CommonException {
        SurveyQuestion question = new SurveyQuestion();
        question.setSeq("0001");

        List<SurveyQuestion> questions = new ArrayList<>();
        questions.add(question);

        QuestionsRequest request = new QuestionsRequest();
        request.setQuestions(questions);

        Mockito.when(surveyQuestionRepository.existsBySeq("0001")).thenReturn(false);

        String actual = service.saveSurveyQuestions(request);

        Mockito.verify(surveyQuestionRepository, Mockito.atLeastOnce()).saveAll(any());
        Assertions.assertEquals("added question fail", actual);
    }

    @Test
    void saveSurveyQuestionsFailWhenDatabaseDownTest() throws CommonException {
        SurveyQuestion question = new SurveyQuestion();
        question.setSeq("0001");

        List<SurveyQuestion> questions = new ArrayList<>();
        questions.add(question);

        QuestionsRequest request = new QuestionsRequest();
        request.setQuestions(questions);

        Mockito.when(surveyQuestionRepository.existsBySeq("0001")).thenThrow(MongoClientException.class);

        Assertions.assertThrows(CommonException.class, () -> service.saveSurveyQuestions(request));

    }

    @Test
    void saveResponsesSuccessTest() throws CommonException {
        SurveyAnswer answerSeq1 = new SurveyAnswer();
        answerSeq1.setSeq("0001");
        answerSeq1.setQuestion("title 1");
        answerSeq1.setAnswer("answer 1");

        SurveyAnswer answerSeq2 = new SurveyAnswer();
        answerSeq2.setSeq("0002");
        answerSeq2.setQuestion("title 2");
        answerSeq2.setAnswer("answer 2");

        SurveyResponseBody surveyResponseBody = new SurveyResponseBody();
        surveyResponseBody.setRespondentId(1150);
        surveyResponseBody.setSurveyAnswer(List.of(answerSeq1, answerSeq2));

        Mockito.when(surveyResponseRepository.existsByRespondentId(1150)).thenReturn(true);

        String actual = service.saveResponses(surveyResponseBody);

        Mockito.verify(surveyResponseRepository, Mockito.atLeast(2)).save(any());
        Assertions.assertEquals("submit response success", actual);
    }

    @Test
    void saveResponsesFailTest() throws CommonException {
        SurveyAnswer answerSeq1 = new SurveyAnswer();
        answerSeq1.setSeq("0001");
        answerSeq1.setQuestion("title 1");
        answerSeq1.setAnswer("answer 1");

        SurveyAnswer answerSeq2 = new SurveyAnswer();
        answerSeq2.setSeq("0002");
        answerSeq2.setQuestion("title 2");
        answerSeq2.setAnswer("answer 2");

        SurveyResponseBody surveyResponseBody = new SurveyResponseBody();
        surveyResponseBody.setRespondentId(1150);
        surveyResponseBody.setSurveyAnswer(List.of(answerSeq1, answerSeq2));

        Mockito.when(surveyResponseRepository.existsByRespondentId(1150)).thenReturn(false);
        String actual = service.saveResponses(surveyResponseBody);

        Assertions.assertEquals("submit response fail", actual);
    }

    @Test
    void saveResponsesFailWhenDatabaseDownTest() throws CommonException {
        SurveyAnswer answerSeq1 = new SurveyAnswer();
        answerSeq1.setSeq("0001");
        answerSeq1.setQuestion("title 1");
        answerSeq1.setAnswer("answer 1");

        SurveyAnswer answerSeq2 = new SurveyAnswer();
        answerSeq2.setSeq("0002");
        answerSeq2.setQuestion("title 2");
        answerSeq2.setAnswer("answer 2");

        SurveyResponseBody surveyResponseBody = new SurveyResponseBody();
        surveyResponseBody.setRespondentId(1150);
        surveyResponseBody.setSurveyAnswer(List.of(answerSeq1, answerSeq2));

        Mockito.doThrow(MongoClientException.class).when(surveyResponseRepository).save(any());

        Assertions.assertThrows(CommonException.class, () -> service.saveResponses(surveyResponseBody));
    }

    @Test
    void getSurveyResponseBySeqSuccessTest() throws CommonException {
        SurveyResponseWithCount response = new SurveyResponseWithCount();
        response.setSeq("0001");
        response.setQuestion("title1");
        response.setAnswer("1");
        response.setCount(5L);

        SurveyResponseWithCount response2 = new SurveyResponseWithCount();
        response2.setSeq("0001");
        response2.setQuestion("title1");
        response2.setAnswer("2");
        response2.setCount(6L);

        List<SurveyResponseWithCount> surveyResponseWithCount = new ArrayList<>();
        surveyResponseWithCount.add(response);
        surveyResponseWithCount.add(response2);

        Mockito.when(surveyResponseRepository.countGroupedBySeqAndAnswer()).thenReturn(surveyResponseWithCount);

        SurveyResponseSummary actual = service.getSurveyResponseBySeq("0001");

        Assertions.assertTrue(actual.getAnswerCounts().get(0).getCount() == 5);
        Assertions.assertTrue(actual.getAnswerCounts().get(1).getCount() == 6);
    }

    @Test
    void getSurveyResponseBySeqWhenNotExistsSeqTest() throws CommonException {
        SurveyResponseWithCount response = new SurveyResponseWithCount();
        response.setSeq("0001");
        response.setQuestion("title1");
        response.setAnswer("1");
        response.setCount(5L);

        SurveyResponseWithCount response2 = new SurveyResponseWithCount();
        response2.setSeq("0001");
        response2.setQuestion("title1");
        response2.setAnswer("2");
        response2.setCount(6L);

        List<SurveyResponseWithCount> surveyResponseWithCount = new ArrayList<>();
        surveyResponseWithCount.add(response);
        surveyResponseWithCount.add(response2);

        Mockito.when(surveyResponseRepository.countGroupedBySeqAndAnswer()).thenReturn(new ArrayList<>());

        SurveyResponseSummary actual = service.getSurveyResponseBySeq("0001");

        Assertions.assertEquals("0001", actual.getSeq());
        Assertions.assertEquals("not found response for seq 0001", actual.getMessage());
    }

    @Test
    void getSurveyResponseBySeqWhenGetMongoExceptionTest() throws CommonException {
        MongoClientException ex = new MongoClientException("timeout");
        Mockito.when(surveyResponseRepository.countGroupedBySeqAndAnswer()).thenThrow(ex);

        Assertions.assertThrows(CommonException.class, () -> service.getSurveyResponseBySeq("0001"));
    }

    @Test
    void getSurveyResponseByRespondentIdSuccessTest() throws CommonException {
        SurveyResponseEntity surveyResponse = new SurveyResponseEntity();
        surveyResponse.setRespondentId(1150);
        surveyResponse.setQuestion("title 1");
        surveyResponse.setAnswer("answer 1");
        surveyResponse.setSeq("0001");

        SurveyResponseEntity surveyResponse2 = new SurveyResponseEntity();
        surveyResponse2.setRespondentId(1150);
        surveyResponse2.setQuestion("title 2");
        surveyResponse2.setAnswer("answer 2");
        surveyResponse2.setSeq("0002");
        List<SurveyResponseEntity> response = List.of(surveyResponse);

        Mockito.when(surveyResponseRepository.findByRespondentId(1150)).thenReturn(response);

        List<SurveyResponseEntity> actual = service.getSurveyResponseByRespondentId("1150");

        Assertions.assertTrue(!actual.isEmpty());

    }

    @Test
    void getSurveyResponseByRespondentIdWhenGetMongoExceptionTest() throws CommonException {
        MongoClientException ex = new MongoClientException("timeout");
        Mockito.when(surveyResponseRepository.findByRespondentId(1)).thenThrow(ex);

        Assertions.assertThrows(CommonException.class, () -> service.getSurveyResponseByRespondentId("1"));

    }
}
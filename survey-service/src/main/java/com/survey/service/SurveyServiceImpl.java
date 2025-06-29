package com.survey.service;

import com.survey.exception.model.CommonException;
import com.survey.model.AnswerCount;
import com.survey.model.QuestionsRequest;
import com.survey.model.SurveyResponseSummary;
import com.survey.model.SurveyResponseWithCount;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyAnswer;
import com.survey.model.surveyresponse.SurveyResponseBody;
import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.repository.jpa.SurveyResponseRepository;
import com.survey.repository.mongo.SurveyQuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.h2.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SurveyServiceImpl implements SurveyService {
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyResponseRepository surveyResponseRepository;

    public SurveyServiceImpl(SurveyQuestionRepository surveyQuestionRepository, SurveyResponseRepository responseRepository) {
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyResponseRepository = responseRepository;
    }

    @Override
    public List<SurveyQuestion> getSurveyQuestion(String seq) throws CommonException {
        try {
            if (StringUtils.isNullOrEmpty(seq)) {
                return surveyQuestionRepository.findAll();
            }

            SurveyQuestion question = surveyQuestionRepository.findBySeq(seq);
            if (question != null) {
                return Collections.singletonList(question);
            }
            log.info("seq {} is not exist in db", seq);
            return Collections.emptyList();

        } catch (Exception e) {
            log.error("fail to get data from mongo db", e);
            throw getCommonException("fail to fetch question");
        }
    }

    @Override
    public String saveSurveyQuestions(QuestionsRequest request) throws CommonException {
        try {
            List<SurveyQuestion> surveyQuestionList = request.getQuestions();
            surveyQuestionRepository.saveAll(surveyQuestionList);
            for (SurveyQuestion question : surveyQuestionList) {
                if (!surveyQuestionRepository.existsBySeq(question.getSeq())) {
                    return "added question fail";
                }
            }
            return "added question success";
        } catch (Exception e) {
            log.error("fail to save data to mongo db", e);
            throw getCommonException("fail to save question to db");
        }
    }

    @Override
    public String saveResponses(SurveyResponseBody surveyResponse) throws CommonException {
        List<SurveyAnswer> responseList = surveyResponse.getSurveyAnswer();
        Integer respondentId = surveyResponse.getRespondentId();
        try {
            for (SurveyAnswer s : responseList) {
                SurveyResponseEntity resp = new SurveyResponseEntity();
                resp.setRespondentId(respondentId);
                resp.setSeq(s.getSeq());
                resp.setQuestion(s.getQuestion());
                resp.setAnswer(s.getAnswer());
                surveyResponseRepository.save(resp);
            }
            if (!surveyResponseRepository.existsByRespondentId(respondentId)) {
                return "submit response fail";
            }
            return "submit response success";
        } catch (Exception e) {
            log.error("fail to save data to db ", e);
            throw getCommonException("fail to save response to db");
        }
    }

    @Override
    public SurveyResponseSummary getSurveyResponseBySeq(String seq) throws CommonException {
        List<SurveyResponseWithCount> responseList = null;
        try {
            responseList = surveyResponseRepository.countGroupedBySeqAndAnswer();
        } catch (Exception e) {
            log.error("fail to get data from db ", e);
            throw getCommonException("fail to get data from db");
        }
        if (responseList != null && !responseList.isEmpty()) {
            List<AnswerCount> answerCountsList = new ArrayList<>();
            String question = "";
            for (SurveyResponseWithCount resp : responseList) {
                if (resp.getSeq().equals(seq)) {
                    question = resp.getQuestion();
                    AnswerCount answerCount = new AnswerCount();
                    answerCount.setAnswer(resp.getAnswer());
                    answerCount.setCount(resp.getCount());
                    answerCountsList.add(answerCount);
                }
            }
            if (!answerCountsList.isEmpty()) {
                SurveyResponseSummary responseSum = new SurveyResponseSummary();
                responseSum.setSeq(seq);
                responseSum.setQuestion(question);
                responseSum.setAnswerCounts(answerCountsList);
                return responseSum;
            }
        } else {
            log.info("not found response for seq requested");
            SurveyResponseSummary res = new SurveyResponseSummary();
            res.setSeq(seq);
            res.setMessage("not found response for seq " + seq);
            return res;
        }
        return null;
    }

    @Override
    public  List<SurveyResponseEntity> getSurveyResponseByRespondentId(String respId) throws CommonException {
        try {
            return surveyResponseRepository.findByRespondentId(Integer.valueOf(respId));
        } catch (Exception e) {
            throw getCommonException("fail to get data from db");
        }
    }

    private CommonException getCommonException(String message) {
        return new CommonException(HttpStatus.INTERNAL_SERVER_ERROR, "failed", message);
    }
}

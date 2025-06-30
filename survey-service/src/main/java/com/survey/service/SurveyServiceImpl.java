package com.survey.service;

import com.survey.exception.model.CommonException;
import com.survey.model.surveyquestion.QuestionsRequestBody;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.AnswerCount;
import com.survey.model.surveyresponse.SurveyAnswer;
import com.survey.model.surveyresponse.SurveyResponse;
import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.model.surveyresponse.SurveyResponseRequestBody;
import com.survey.model.surveyresponse.SurveyResponseSummary;
import com.survey.model.surveyresponse.SurveyResponseWithCount;
import com.survey.repository.jpa.SurveyResponseRepository;
import com.survey.repository.mongo.SurveyQuestionRepository;
import lombok.extern.slf4j.Slf4j;
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
            SurveyQuestion question = surveyQuestionRepository.findBySeq(seq);
            if (question == null) {
                log.info("seq {} is not exist in db", seq);
                throw new CommonException(HttpStatus.NOT_FOUND, "failed", "requested seq is not exist in db");
            }
            return Collections.singletonList(question);

        } catch (Exception e) {
            if (e instanceof CommonException) {
                throw e;
            }
            log.error("fail to get data from mongo db", e);
            throw getCommonException("fail to fetch question");
        }
    }

    @Override
    public String saveSurveyQuestions(QuestionsRequestBody request) throws CommonException {
        try {
            List<SurveyQuestion> surveyQuestionList = request.getQuestions();
            surveyQuestionRepository.saveAll(surveyQuestionList);
            return "added question success";
        } catch (Exception e) {
            log.error("added question fail", e);
            throw getCommonException("fail to save question to db");
        }
    }

    @Override
    public String saveResponses(SurveyResponseRequestBody surveyResponse) throws CommonException {
        List<SurveyAnswer> responseList = surveyResponse.getSurveyAnswer();
        Integer respondentId = surveyResponse.getRespondentId();
        try {
            log.debug("start save for respondent id {}", respondentId);
            for (SurveyAnswer s : responseList) {
                SurveyResponseEntity resp = new SurveyResponseEntity();
                resp.setRespondentId(respondentId);
                resp.setSeq(s.getSeq());
                resp.setQuestion(s.getQuestion());
                resp.setAnswer(s.getAnswer());
                surveyResponseRepository.save(resp);
                log.debug("saved response for seq {}", s.getSeq());
            }
            return "submit response success for respondent id " + respondentId;
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
    public List<SurveyResponse> getSurveyResponseByRespondentId(String respId) throws CommonException {
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

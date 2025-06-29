package com.survey.repository.mongo;

import com.survey.model.surveyquestion.SurveyQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestionRepository extends MongoRepository<SurveyQuestion, String> {
    SurveyQuestion findBySeq(String seq);
    boolean existsBySeq(String seq);
}

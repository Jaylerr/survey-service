package com.survey.repository.jpa;

import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.model.SurveyResponseWithCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponseEntity, String> {
    List<SurveyResponseEntity> findByRespondentId(Integer respondentId);

    boolean existsByRespondentId(Integer respondentId);

    @Query(value =
    """
    SELECT 
        SEQ as seq,
        QUESTION as question,
        ANSWER as answer,
        COUNT(*) as count
    FROM SURVEY_RESPONSES
    GROUP BY SEQ, QUESTION, ANSWER
    ORDER BY SEQ, QUESTION
    """, nativeQuery = true)
    List<SurveyResponseWithCount> countGroupedBySeqAndAnswer();
}
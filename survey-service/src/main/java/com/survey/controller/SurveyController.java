package com.survey.controller;

import com.survey.exception.model.CommonException;
import com.survey.model.QuestionsRequest;
import com.survey.model.surveyquestion.SurveyQuestion;
import com.survey.model.surveyresponse.SurveyResponseBody;
import com.survey.model.SurveyResponseSummary;
import com.survey.model.surveyresponse.SurveyResponseEntity;
import com.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/survey-app")
@Validated
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @Operation(summary = "Fetch question", description = "for get question from db")
    @PostMapping()
    public ResponseEntity<List<SurveyQuestion>> fetchSurveyQuestion(
            @RequestParam() String seq) throws CommonException {
        return ResponseEntity.ok(surveyService.getSurveyQuestion(seq));
    }

    @Operation(summary = "Submit response", description = "for save response to db")
    @PostMapping(value = "/submit")
    public ResponseEntity<String> submitResponse(
            @Valid @RequestBody SurveyResponseBody response) throws CommonException {
        return ResponseEntity.ok(surveyService.saveResponses(response));
    }

    @Operation(summary = "Get response summary", description = "for get and summarize response")
    @GetMapping(value = "/response/seq/{seq}")
    public ResponseEntity<SurveyResponseSummary> getResponseBySeq(
            @PathVariable String seq) throws CommonException {
        return ResponseEntity.ok(surveyService.getSurveyResponseBySeq(seq));
    }

    @Operation(summary = "Get response summary", description = "for get and summarize response")
    @GetMapping(value = "/response/respondent-id/{respId}")
    public ResponseEntity<List<SurveyResponseEntity>> getResponseByRespondentId(
            @PathVariable String respId) throws CommonException {
        return ResponseEntity.ok(surveyService.getSurveyResponseByRespondentId(respId));
    }

    @Operation(summary = "Add new question", description = "for save question to db")
    @PostMapping(value = "/add-questions")
    public ResponseEntity<String> addNewQuestions(
            @RequestBody QuestionsRequest request) throws CommonException {
        return ResponseEntity.ok(surveyService.saveSurveyQuestions(request));
    }
}
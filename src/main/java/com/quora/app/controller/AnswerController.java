package com.quora.app.controller;

import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import com.quora.app.dto.QuestionAnswerDTO;
import com.quora.app.service.IAnswerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/answer")
@Validated
public class AnswerController {

    private final IAnswerService answerService;

    @PostMapping
    public Mono<AnswerResponseDTO> createAnswer(@RequestBody @Valid AnswerRequestDTO answerRequestDTO){

        return answerService.createAnswer(answerRequestDTO)
                .doOnSuccess((x)->log.info("Able to create Answer"))
                .doOnError((error)-> log.error("Unable to create Answer getting {}",error.getMessage()));

    }

    @GetMapping("/question/{questionId}")
    public Mono<QuestionAnswerDTO> getAnswerByQuestionID(
            @PathVariable("questionId")
            @NotBlank(message = "QuestionID must not be null or blank")
            String questionId) {
        return answerService.getAnswersByQuestionId(questionId)
                .doOnSuccess((x) -> log.info("Able to fetch Answer"))
                .doOnError((error) -> log.error("Unable to fetch Answer: {}", error.getMessage()));
    }

    @DeleteMapping("/{answerId}")
    public Mono<String> deleteAnswerByID(@PathVariable String answerId){

        return answerService.deleteAnswerByID(answerId)
                .doOnSuccess((x)-> log.info("Able to delete"))
                .doOnError((error)-> log.error("Something went wrong "));
    }

    @GetMapping("/{answerId}")
    public Mono<AnswerResponseDTO> getAnswerById(@PathVariable  String answerId){
           return answerService.getAnswerByID(answerId)
                   .doOnSuccess((x) -> log.info("Able to fetch Answer"))
                   .doOnError((error) -> log.error("Unable to fetch Answer: {}", error.getMessage()));


    }

}

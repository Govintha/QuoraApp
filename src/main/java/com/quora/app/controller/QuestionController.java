package com.quora.app.controller;

import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import com.quora.app.entity.Question;
import com.quora.app.service.IQuestionService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final IQuestionService questionService;

    @GetMapping("/author/{authorId}")
    public Flux<QuestionResponseDTO> getQuestionsByAuthorId(@PathVariable String authorId){
      return questionService.getAllQuestionByAuthorId(authorId);
    }

    @PostMapping
    public Mono<QuestionResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDTO){
        return questionService.createQuestion(questionRequestDTO)
                .doOnSuccess(response-> log.info("Success Fully Created "))
                .doOnError(error-> log.error("Something went wrong while save question {}",error.getMessage()));
    }

    @GetMapping("/{questionID}")
    public Mono<QuestionResponseDTO> getQuestionByID(@PathVariable String questionID){
        return questionService.getQuestionById(questionID)
                .doOnSuccess(response-> log.info("Success Fully Created "))
                .doOnError(error-> log.error("Something went wrong while save question {}",error.getMessage()));
    }

    @GetMapping
    public Flux<QuestionResponseDTO> getAllQuestions(){
        return questionService.getAllQuestions()
                .doOnComplete(()->log.info("Success Fully Created "))
                .doOnError(error->log.info("Something went wrong while save question {}",error.getMessage()));

    }

    @DeleteMapping("/{questionId}")
    public Mono<String> deleteQuestionByID(@PathVariable String questionId){
        return questionService.deleteQuestionByID(questionId)
                .doOnSuccess((response)->log.info("Success Fully Deleted "))
                .doOnError(error->log.info("Something went wrong unable to delete {}",error.getMessage()));

    }
}

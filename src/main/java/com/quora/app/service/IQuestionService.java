package com.quora.app.service;

import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import com.quora.app.entity.Question;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {

    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionDTO);
    Flux<QuestionResponseDTO> getAllQuestionByAuthorId(String authorId);
    Mono<QuestionResponseDTO> getQuestionById(String questionId);
    Flux<QuestionResponseDTO> getAllQuestions();
    Mono<String> deleteQuestionByID(String id);
}

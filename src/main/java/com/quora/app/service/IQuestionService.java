package com.quora.app.service;

import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IQuestionService {

    Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionDTO);
    Flux<QuestionResponseDTO> getAllQuestionByAuthorId(String authorId);
    Mono<QuestionResponseDTO> getQuestionById(String questionId);
    Flux<QuestionResponseDTO> getAllQuestions(String cursor,int size);
    Mono<String> deleteQuestionByID(String id);
    Flux<QuestionResponseDTO> searchTitleORContent(String searchText,int offset,int pageSize);

}

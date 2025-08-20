package com.quora.app.service;

import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import com.quora.app.dto.QuestionAnswerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAnswerService {

    Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO answerRequestDTO);
    Mono<String> deleteAnswerByID(String answerID);
    Mono<AnswerResponseDTO> getAnswerByID(String id);
    Mono<QuestionAnswerDTO> getAnswersByQuestionId(String questionID);

}

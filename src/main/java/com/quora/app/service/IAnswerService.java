package com.quora.app.service;

import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import reactor.core.publisher.Mono;

public interface IAnswerService {

    Mono<AnswerRequestDTO> createAnswer(AnswerRequestDTO answerRequestDTO);
    Mono<AnswerResponseDTO> deleteAnswer(String answerID);
    Mono<AnswerResponseDTO> getAnswerByID(String id);

}

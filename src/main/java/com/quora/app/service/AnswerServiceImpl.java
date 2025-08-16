package com.quora.app.service;

import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AnswerServiceImpl implements IAnswerService{


    @Override
    public Mono<AnswerRequestDTO> createAnswer(AnswerRequestDTO answerRequestDTO) {
        return null;
    }

    @Override
    public Mono<AnswerResponseDTO> deleteAnswer(String answerID) {
        return null;
    }

    @Override
    public Mono<AnswerResponseDTO> getAnswerByID(String id) {
        return null;
    }
}

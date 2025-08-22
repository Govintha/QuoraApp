package com.quora.app.service.impl;

import com.quora.app.adapter.AnswerMapper;
import com.quora.app.adapter.QuestionAnswerMapper;
import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import com.quora.app.dto.QuestionAnswerDTO;
import com.quora.app.repository.AnswerRepository;
import com.quora.app.repository.QuestionRepository;
import com.quora.app.service.IAnswerService;
import com.quora.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements IAnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final IUserService userService;

    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO answerRequestDTO) {
        userService.getUser(answerRequestDTO.getUserId());
        return answerRepository.save(AnswerMapper.toEntity(answerRequestDTO))
                .flatMap(savedAnswer ->
                        questionRepository.findById(savedAnswer.getQuestionId())
                                .flatMap(question -> {
                                    if (question.getAnswerID()== null) {
                                        question.setAnswerID(new ArrayList<>());
                                    }
                                    question.getAnswerID().add(savedAnswer.getId());
                                    return questionRepository.save(question)
                                            .thenReturn(savedAnswer);
                                })
                )
                .map(AnswerMapper::toAnswerResponseDTO)
                .doOnSuccess(response -> log.info("Successfully Created"))
                .doOnError(error -> log.error("Unable to create Answer", error));
    }

    @Override
    public Mono<String> deleteAnswerByID(String answerID) {

        return answerRepository.findById(answerID)
                .switchIfEmpty(Mono.error(new RuntimeException("Answer not found")))
                .flatMap(answer -> removeAnswerFromQuestion(answer.getQuestionId(), answerID)
                                .then(answerRepository.deleteById(answerID))
                                .thenReturn("Answer Successfully Deleted from Question and Answer")
                )
                .doOnSuccess(res -> log.info(res))
                .doOnError(error -> log.error("Error deleting answer {}: {}", answerID, error.getMessage()));
    }



    @Override
    public Mono<AnswerResponseDTO> getAnswerByID(String id) {
        return answerRepository.findById(id)
                .map(AnswerMapper::toAnswerResponseDTO)
                .doOnSuccess((response)-> log.info("SuccessFully Fetch Anseer"))
                .doOnError((error)->log.error("Unable to fetch answer due to {}",error.getMessage()));
    }

    @Override
    public Mono<QuestionAnswerDTO> getAnswersByQuestionId(String questionID) {
        return answerRepository.findByQuestionId(questionID)
                .collectList()
                .map((answers)->QuestionAnswerMapper.toReponse(answers,questionID))
                .doOnSuccess((response)->log.info("Able to get Answers"))
                .doOnError((error)->log.error("Something went wrong getting {}",error.getMessage()));
    }

    public Mono<Void> removeAnswerFromQuestion(String questionId, String answerId) {
        return questionRepository.findById(questionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Question not found")))
                .flatMap(question -> {
                    question.getAnswerID().remove(answerId); // remove in Java
                    return questionRepository.save(question).then();
                });
    }

}

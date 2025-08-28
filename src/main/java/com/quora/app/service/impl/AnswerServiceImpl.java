package com.quora.app.service.impl;

import com.quora.app.adapter.AnswerMapper;
import com.quora.app.adapter.QuestionAnswerMapper;
import com.quora.app.dto.AnswerRequestDTO;
import com.quora.app.dto.AnswerResponseDTO;
import com.quora.app.dto.FeedEntry;
import com.quora.app.dto.QuestionAnswerDTO;
import com.quora.app.enumaration.TargetType;
import com.quora.app.events.ViewCountEvent;
import com.quora.app.kafka.producer.KafkaEventProducerService;
import com.quora.app.repository.AnswerRepository;
import com.quora.app.repository.QuestionRepository;
import com.quora.app.service.IAnswerService;
import com.quora.app.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements IAnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final IUserService userService;
    private final KafkaEventProducerService kafkaProducerService;
    private final CacheFeedService cacheFeedService;


    @Override
    public Mono<AnswerResponseDTO> createAnswer(AnswerRequestDTO answerRequestDTO) {
        return userService.getUser(answerRequestDTO.getUserId())
                //Check if the user exists
                .switchIfEmpty(Mono.error(new RuntimeException("User Not Found")))
                //  If user exists continue
                .flatMap(user ->
                        //  Save the answer into DB
                        answerRepository.save(AnswerMapper.toEntity(answerRequestDTO))

                                //  After saving answer, update the related Question
                                .flatMap(savedAnswer ->
                                        questionRepository.findById(savedAnswer.getQuestionId())
                                                .flatMap(question -> {
                                                    //Ensure answer list exists
                                                    if (question.getAnswerID() == null) {
                                                        question.setAnswerID(new ArrayList<>());
                                                    }
                                                    // Add this new answerId
                                                    question.getAnswerID().add(savedAnswer.getId());

                                                    // Save updated question and return savedAnswer
                                                    return questionRepository.save(question)
                                                            .thenReturn(savedAnswer);
                                                })
                                )

                                //  Add this answer to the Feed cache
                                .flatMap(savedAnswer -> {
                                    FeedEntry entry = new FeedEntry(
                                            TargetType.ANSWER,
                                            savedAnswer.getId(),
                                            savedAnswer.getCreatedAt()
                                    );

                                    //  Fanout to followers
                                    Mono<Void> fanout =
                                            cacheFeedService.distributePostToFollowers(savedAnswer.getUserId(), entry);

                                    return Mono.when(fanout)
                                            .thenReturn(savedAnswer);
                                })
                ).map(AnswerMapper::toAnswerResponseDTO)
                .doOnSuccess(response -> log.info("Successfully Created Answer"))
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
                .doOnSuccess((response)-> {
                    kafkaProducerService.sendViewEvent(
                            ViewCountEvent.builder()
                                    .targetId(id)
                                    .targetType(TargetType.ANSWER)
                                    .viewedAt(Instant.now())
                                    .build());
                })

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

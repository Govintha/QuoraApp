package com.quora.app.service.impl;

import com.quora.app.adapter.QuestionMapper;
import com.quora.app.dto.FeedEntry;
import com.quora.app.dto.QuestionRequestDTO;
import com.quora.app.dto.QuestionResponseDTO;
import com.quora.app.entity.Question;
import com.quora.app.enumaration.TargetType;
import com.quora.app.events.ViewCountEvent;
import com.quora.app.kafka.producer.KafkaEventProducerService;
import com.quora.app.repository.QuestionRepository;
import com.quora.app.service.IQuestionService;
import com.quora.app.service.IUserService;
import com.quora.app.utils.CursorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements IQuestionService {

    private final QuestionRepository questionRepository;
    private final KafkaEventProducerService kafkaProducerService;
    private final IUserService userService;
    private final CacheFeedService feedCacheService;

    public Mono<QuestionResponseDTO> createQuestion(QuestionRequestDTO questionDTO) {
        return userService.getUser(questionDTO.getUserId())
                .switchIfEmpty(Mono.error(new RuntimeException("User Not found")))
                .flatMap(user -> {
                    Question entity = QuestionMapper.toEntity(questionDTO);

                    return questionRepository.save(entity)
                            .map(QuestionMapper::toResponseDTO)
                            .flatMap(response -> {
                                FeedEntry entry = new FeedEntry(
                                        TargetType.QUESTION,
                                        entity.getId(),
                                        entity.getCreatedAt()
                                );

//                                //  Add to author’s own feed
//                                Mono<Void> addToAuthorFeed =
//                                        feedCacheService.storeFeedEntry(entity.getUserId(), entry);

                                //  Fanout to followers
                                Mono<Void> fanout =
                                        feedCacheService.distributePostToFollowers(entity.getUserId(), entry);

                                return Mono.when( fanout) // run both
                                        .thenReturn(response);
                            });
                });
    }



    @Override
    public Flux<QuestionResponseDTO> getAllQuestionByAuthorId(String authorId) {
        return null;
    }

    @Override
    public Mono<QuestionResponseDTO> getQuestionById(String questionId) {
        return questionRepository
                .findById(questionId)
                .map(QuestionMapper::toResponseDTO)
                .doOnSuccess(response->{{
                    log.info("Success Fully fetched ");
                    kafkaProducerService.sendViewEvent(
                                     ViewCountEvent.builder()
                                    .targetId(questionId)
                                    .targetType(TargetType.QUESTION)
                                    .viewedAt(Instant.now())
                                     .build());
                }
                })
                .doOnError(error-> log.error("Something went wrong while fetched question {}",error.getMessage()));

    }

    @Override
    public Flux<QuestionResponseDTO> getAllQuestions(String cursor,int size) {

        Pageable pageable=PageRequest.of(0,size);

        if(CursorUtils.isValidCursor(cursor)) {

            Instant timeStamp=CursorUtils.parseCursor(cursor);
            return questionRepository
                    .findByCreatedAtGreaterThanOrderByCreatedAtAsc(timeStamp,pageable)
                    .map(QuestionMapper::toResponseDTO)
                    .doOnComplete(() -> log.info("Success Fully fetched"))
                    .doOnError(error -> log.info("Something went wrong while fetched question {}", error.getMessage()));
        }else{

           return questionRepository.findAllByOrderByCreatedAtAsc(pageable)
                   .map(QuestionMapper::toResponseDTO)
                   .doOnComplete(() -> log.info("Success Fully fetched"))
                   .doOnError(error -> log.info("Something went wrong while fetched question {}", error.getMessage()));

        }

    }

    @Override
    public Mono<String> deleteQuestionByID(String id) {
        return questionRepository
                .deleteById(id)
                .thenReturn("Successfully Deleted the Question") // emit your message
                .doOnSuccess((response)->log.info("Success Fully Deleted"))
                .doOnError((error)->log.info("Something went wrong unable to delete {}",error.getMessage()));
    }

    @Override
    public Flux<QuestionResponseDTO> searchTitleORContent(String searchText, int offset, int pageSize) {
        return questionRepository.findByContentOrTitleContainingIgnoreCase(searchText, PageRequest.of(offset,pageSize))
                .map(QuestionMapper::toResponseDTO)
                .doOnComplete(()->log.info("Success Fully fetched"))
                .doOnError(error->log.info("Something went wrong while fetched question {}",error.getMessage()));
    }

    @Override
    public Flux<QuestionResponseDTO> getQuestionByTagName(String tagName) {

        return questionRepository.findByTagsTag(tagName)
                .map(QuestionMapper::toResponseDTO)
                .doOnComplete(()->log.info("Success Fully fetched"))
                .doOnError(error->log.info("Something went wrong while fetched question {}",error.getMessage()));

    }

}

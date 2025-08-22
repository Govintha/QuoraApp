package com.quora.app.repository;


import com.quora.app.entity.Answer;
import com.quora.app.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {

    Flux<Answer> findByQuestionId(String questionId);
    Flux<Answer> findByUserIdInAndCreatedAtLessThanOrderByCreatedAtDesc(
            List<Integer> userIds, Instant cursor, Pageable pageable
    );
    Flux<Answer> findByTags_TagInAndCreatedAtBeforeOrderByCreatedAtDesc(
            List<String> tags, Instant cursor, Pageable pageable
    );

}

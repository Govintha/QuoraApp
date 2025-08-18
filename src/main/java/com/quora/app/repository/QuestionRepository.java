package com.quora.app.repository;

import com.quora.app.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDateTime;


@Repository
public interface QuestionRepository extends ReactiveMongoRepository<Question,String> {

    @Query("{ '$or': [ { 'title': { $regex: ?0, $options: 'i' } }, { 'content': { $regex: ?0, $options: 'i' } } ] }")
    Flux<Question> findByContentOrTitleContainingIgnoreCase(String searchText, Pageable pageable);

    Flux<Question> findByCreatedAtGreaterThanOrderByCreatedAtAsc(Instant cursor,Pageable pageable);

    Flux<Question> findAllByOrderByCreatedAtAsc(Pageable pageable);

    Flux<Question> findByTagsTag(String tag);

}

package com.quora.app.repository;


import com.quora.app.entity.Answer;
import com.quora.app.entity.Question;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {

    Flux<Answer> findByQuestionId(String questionId);
    Flux<Answer> findByUserIdInOrderByCreatedAtDesc(List<Integer> userIds);

}

package com.quora.app.repository;


import com.quora.app.entity.Answer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {

    Flux<Answer> findByQuestionId(String questionId);
}

package com.quora.app.repository;


import com.quora.app.entity.Answer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends ReactiveMongoRepository<Answer,String> {
}

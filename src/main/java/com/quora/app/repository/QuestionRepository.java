package com.quora.app.repository;

import com.quora.app.entity.Question;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface QuestionRepository extends ReactiveMongoRepository<Question,String> {

}

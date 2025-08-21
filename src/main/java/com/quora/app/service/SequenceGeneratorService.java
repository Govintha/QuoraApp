package com.quora.app.service;

import com.quora.app.entity.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SequenceGeneratorService {

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    public Mono<Integer> getNextSequence(String seqName) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        return mongoOperations.findAndModify(query, update, options, Counter.class)
                .map(Counter::getSeq);
    }
}

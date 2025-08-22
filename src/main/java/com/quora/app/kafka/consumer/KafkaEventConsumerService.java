package com.quora.app.kafka.consumer;

import com.quora.app.entity.Question;
import com.quora.app.enumaration.TargetType;
import com.quora.app.events.ViewCountEvent;
import com.quora.app.repository.AnswerRepository;
import com.quora.app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaEventConsumerService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @KafkaListener(topics = "test", groupId = "my-group")
    public void consume(String message) {

        System.out.println("Consumed: " + message);
    }

    @KafkaListener(topics = "views", groupId = "my-group")
    public void consumeViewEvent(ViewCountEvent viewCountEvent) {
        log.info("Able to consume {}",viewCountEvent);
        if(viewCountEvent.getTargetType()== TargetType.QUESTION) {
            questionRepository.findById(viewCountEvent.getTargetId())
                    .flatMap(entity -> {
                        entity.incrementViews();
                        return questionRepository.save(entity);
                    }).doOnSuccess((response) -> {
                        log.info("SuccessFully Increment Count {}", response.getViews());
                    }).doOnError((error) -> {
                        log.error("Unable increment Count due to {}", error.getMessage());
                    }).subscribe();
        }else{
            answerRepository.findById(viewCountEvent.getTargetId())
                    .flatMap(entity -> {
                        entity.incrementViews();
                        return answerRepository.save(entity);
                    }).doOnSuccess((response) -> {
                        log.info("SuccessFully Increment Count {}", response.getViews());
                    }).doOnError((error) -> {
                        log.error("Unable increment Count due to {}", error.getMessage());
                    }).subscribe();
        }
    }
}

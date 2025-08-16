package com.quora.app.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test", groupId = "my-group")
    public void consume(String message) {
        System.out.println("Consumed: " + message);
    }
}

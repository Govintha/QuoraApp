package com.quora.app.controller;

import com.quora.app.kafka.producer.KafkaEventProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaEventProducerService producer;

    public KafkaController(KafkaEventProducerService producer) {
        this.producer = producer;
    }

    @GetMapping("/publish")
    public String publish(@RequestParam String message) {
        producer.sendMessage(message);
        return "Message sent: " + message;
    }
}

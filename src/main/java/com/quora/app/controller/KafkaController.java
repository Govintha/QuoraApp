package com.quora.app.controller;

import com.quora.app.kafka.producer.KafkaProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService producer;

    public KafkaController(KafkaProducerService producer) {
        this.producer = producer;
    }

    @GetMapping("/publish")
    public String publish(@RequestParam String message) {
        producer.sendMessage(message);
        return "Message sent: " + message;
    }
}

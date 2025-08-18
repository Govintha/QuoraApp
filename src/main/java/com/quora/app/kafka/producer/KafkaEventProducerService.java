package com.quora.app.kafka.producer;

import com.quora.app.events.ViewCountEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaEventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send("test", message);
        log.info("Produced: {}", message);
    }

    public void sendViewEvent(ViewCountEvent viewCountEvent) {
        kafkaTemplate.send("views", viewCountEvent)
                        .whenComplete((response,error)->{
                             if(error!=null){
                                  log.error("Something went not product event for "+viewCountEvent.getTargetId()+" {}",error.getMessage());
                             }
                        });
        log.info("ProducedViewCount: {}" , viewCountEvent);
    }


}

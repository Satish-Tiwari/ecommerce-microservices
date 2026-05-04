package com.ecommerce.user_service.service;

import com.ecommerce.user_service.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserEvent(String topic, UserEvent event) {
        log.info("Sending User Event to topic {}: {}", topic, event);
        kafkaTemplate.send(topic, event);
    }
}

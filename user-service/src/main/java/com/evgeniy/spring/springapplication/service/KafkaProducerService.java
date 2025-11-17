package com.evgeniy.spring.springapplication.service;


import com.evgeniy.spring.springapplication.DTO.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendUserCreatedEvent(String email, String name, Long userId) {
        UserEvent event = new UserEvent(email, "CREATED");
        kafkaTemplate.send("user-events", event);
        log.info("Sent USER_CREATED event for user: {}", email);
    }

    public void sendUserDeletedEvent(String email, String name, Long userId) {
        UserEvent event = new UserEvent(email, "DELETED");
        kafkaTemplate.send("user-events", event);
        log.info("Sent USER_DELETED event for user: {}", email);
    }
}

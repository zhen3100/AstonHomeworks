package com.evgeniy.spring.springapplication;

import com.evgeniy.spring.springapplication.DTO.UserEvent;
import com.evgeniy.spring.springapplication.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void sendUserCreatedEvent_ShouldSendCorrectEvent() {
        kafkaProducerService.sendUserCreatedEvent("test@example.com", "Test User", 1L);

        verify(kafkaTemplate).send(eq("user-events"), any(UserEvent.class));
    }

    @Test
    void sendUserDeletedEvent_ShouldSendCorrectEvent() {
        kafkaProducerService.sendUserDeletedEvent("test@example.com", "Test User", 1L);

        verify(kafkaTemplate).send(eq("user-events"), any(UserEvent.class));
    }
}

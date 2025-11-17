package com.evgeniy.spring.springapplication.service;


import com.evgeniy.spring.springapplication.DTO.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки событий пользователя в Kafka
 * Обеспечивает асинхронную коммуникацию между микросервисами
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaProducerService {

    /**
     * KafkaTemplate для отправки сообщений в Kafka
     * String как ключ и UserEvent как значение
     */
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    /**
     * Отправляет событие создания пользователя в Kafka
     *
     * @param email email созданного пользователя
     * @param name имя созданного пользователя
     * @param userId уникальный идентификатор созданного пользователя
     */
    public void sendUserCreatedEvent(String email, String name, Long userId) {
        UserEvent event = new UserEvent(email, "CREATED");
        kafkaTemplate.send("user-events", event);
        log.info("Sent USER_CREATED event for user: {}", email);
    }

    /**
     * Отправляет событие удаления пользователя в Kafka
     *
     * @param email email удаленного пользователя
     * @param name имя удаленного пользователя
     * @param userId уникальный идентификатор удаленного пользователя
     */
    public void sendUserDeletedEvent(String email, String name, Long userId) {
        UserEvent event = new UserEvent(email, "DELETED");
        kafkaTemplate.send("user-events", event);
        log.info("Sent USER_DELETED event for user: {}", email);
    }
}

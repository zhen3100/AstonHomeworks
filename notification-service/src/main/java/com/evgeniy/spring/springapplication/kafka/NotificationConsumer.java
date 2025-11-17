package com.evgeniy.spring.springapplication.kafka;

import com.evgeniy.spring.springapplication.DTO.UserEvent;
import com.evgeniy.spring.springapplication.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Log4j2
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "user-events")
    public void consumeUserEvent(UserEvent event) {

        String subject;
        String message;

        log.info("Received user event: {} for email: {}", event.getAction(), event.getEmail());

        switch (event.getAction()) {
            case "CREATED":
                subject = "Аккаунт успешно создан";
                message = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
                break;
            case "DELETED":
                subject = "Аккаунт удален";
                message = "Здравствуйте! Ваш аккаунт был удалён.";
                break;
            default:
                log.warn("Unknown action type: {}", event.getAction());
                return;
        }

        emailService.sendEmail(event.getEmail(), subject, message);
    }
}

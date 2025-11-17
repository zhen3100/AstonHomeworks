package com.evgeniy.spring.springapplication;

import com.evgeniy.spring.springapplication.DTO.UserEvent;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@ContextConfiguration(classes = TestMailConfig.class)
@Log4j2
@Testcontainers
class NotificationServiceIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:latest")
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private GreenMail greenMail;

    @BeforeEach
    void setUp() throws FolderException {
        // Очищаем почту перед каждым тестом
        greenMail.purgeEmailFromAllMailboxes();
        // Ждем завершения очистки
        await().atMost(2, SECONDS).until(() ->
                greenMail.getReceivedMessages().length == 0
        );
    }

    @AfterEach
    void tearDown() throws FolderException {
        // Чистим еще раз
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    void shouldProcessUserCreatedEventAndSendEmail() throws Exception {
        UserEvent event = new UserEvent("newuser@example.com", "CREATED");

        kafkaTemplate.send("user-events", event);

        await().atMost(30, SECONDS).until(() ->
                greenMail.getReceivedMessages().length == 1
        );

        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals("Аккаунт успешно создан", message.getSubject());
        assertEquals("newuser@example.com", message.getAllRecipients()[0].toString());
    }

    @Test
    void shouldProcessUserDeletedEventAndSendEmail() throws Exception {
        UserEvent event = new UserEvent("olduser@example.com", "DELETED");

        kafkaTemplate.send("user-events", event);

        await().atMost(30, SECONDS).until(() ->
                greenMail.getReceivedMessages().length == 1
        );

        MimeMessage message = greenMail.getReceivedMessages()[0];
        assertEquals("Аккаунт удален", message.getSubject());
        assertEquals("olduser@example.com", message.getAllRecipients()[0].toString());
    }

}
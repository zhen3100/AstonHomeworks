package com.evgeniy.spring.springapplication;

import com.evgeniy.spring.springapplication.service.EmailService;
import com.icegreen.greenmail.util.GreenMail;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(TestMailConfig.class)
@TestPropertySource(properties = {
        "spring.mail.host=localhost",
        "spring.mail.port=3025"
})
class EmailServiceIntegrationTest {
    @Autowired
    private EmailService emailService;

    @Autowired
    private GreenMail greenMail;

    @Test
    void shouldSendEmailSuccessfully() throws Exception {
        emailService.sendEmail("test@example.com", "Test Subject", "Test Message");

        greenMail.waitForIncomingEmail(5000, 1);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage message = receivedMessages[0];

        assertEquals("Test Subject", message.getSubject());
        assertEquals("test@example.com", message.getAllRecipients()[0].toString());
        assertTrue(message.getContent().toString().contains("Test Message"));
    }

}

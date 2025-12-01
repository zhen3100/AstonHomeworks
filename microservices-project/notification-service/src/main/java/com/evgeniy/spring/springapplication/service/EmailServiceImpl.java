package com.evgeniy.spring.springapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@example.com");

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendUserCreatedNotification(String email, String userName) {
        String subject = "Добро пожаловать!";
        String message = String.format(
                "Здравствуйте, %s! Ваш аккаунт на сайте был успешно создан.",
                userName
        );
        sendEmail(email, subject, message);
    }

    public void sendUserDeletedNotification(String email, String userName) {
        String subject = "Аккаунт удален";
        String message = String.format(
                "Здравствуйте, %s! Ваш аккаунт был удалён.",
                userName
        );
        sendEmail(email, subject, message);
    }
}

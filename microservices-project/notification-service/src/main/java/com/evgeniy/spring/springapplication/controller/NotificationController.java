package com.evgeniy.spring.springapplication.controller;


import com.evgeniy.spring.springapplication.DTO.EmailRequest;
import com.evgeniy.spring.springapplication.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendEmail(request.getTo(), request.getSubject(), request.getMessage());
        return ResponseEntity.ok("Письмо успешно отправлено");
    }

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestParam String email,
                                                   @RequestParam String userName) {
        emailService.sendUserCreatedNotification(email, userName);
        return ResponseEntity.ok("Приветственное письмо отправлено успешно");
    }

    @PostMapping("/account-deleted")
    public ResponseEntity<String> sendAccountDeletedEmail(@RequestParam String email,
                                                          @RequestParam String userName) {
        emailService.sendUserDeletedNotification(email, userName);
        return ResponseEntity.ok("Письмо об удалении аккаунта отправлено успешно");
    }
}

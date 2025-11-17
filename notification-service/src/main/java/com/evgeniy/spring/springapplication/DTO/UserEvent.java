package com.evgeniy.spring.springapplication.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEvent {
    private String email;
    private String action; // "CREATED" или "DELETED"
    private LocalDateTime timestamp;

    public UserEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public UserEvent(String email, String action) {
        this();
        this.email = email;
        this.action = action;
    }
}

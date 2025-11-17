package com.evgeniy.spring.springapplication.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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

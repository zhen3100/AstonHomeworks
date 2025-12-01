package com.evgeniy.spring.springapplication.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Событие пользователя для передачи через Kafka между микросервисами
 * содержит информацию о действии с пользователем (создание/удаление)
 * используется для асинхронной коммуникации между User Service и Notification Service
 */
@Data
public class UserEvent {

    /**
     * Email пользователя, над которым выполнено действие
     * не может быть null
     */
    private String email;

    /**
     * Тип выполненного действия
     * допустимые значения: "CREATED" (создание) или "DELETED" (удаление)
     */
    private String action; // "CREATED" или "DELETED"

    /**
     * Временная метка создания события
     * устанавливается автоматически при создании объекта
     */
    private LocalDateTime timestamp;

    /**
     * Конструктор по умолчанию
     * инициализирует временную метку текущим временем
     */
    public UserEvent() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Конструктор с параметрами email и action
     *
     * @param email email пользователя, не может быть null
     * @param action тип действия ("CREATED" или "DELETED")
     */
    public UserEvent(String email, String action) {
        this();
        this.email = email;
        this.action = action;
    }
}
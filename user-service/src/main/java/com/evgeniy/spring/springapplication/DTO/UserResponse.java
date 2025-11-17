package com.evgeniy.spring.springapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO для возвращения данных о пользователе
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * Уникальный идентификатор пользователя
     */
    private Long id;

    /**
     * Имя пользователя
     */
    private String name;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Возраст пользователя
     */
    private Integer age;

    /**
     * Дата и время создания пользователя
     */
    private LocalDateTime createdAt;

}

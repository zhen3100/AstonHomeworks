package com.evgeniy.spring.springapplication.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с данными пользователя")
public class UserResponse {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    private String name;

    @Schema(description = "Email адрес пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Возраст пользователя", example = "30")
    private Integer age;

    @Schema(description = "Дата и время создания пользователя", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}
package com.evgeniy.spring.springapplication.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "Имя пользователя", example = "Иван Иванов", minLength = 2, maxLength = 100)
    private String name;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 150)
    @Schema(description = "Email адрес пользователя", example = "user@example.com")
    private String email;

    @Min(value = 0)
    @Max(value = 150)
    @Column(name = "age", nullable = false)
    @Schema(description = "Возраст пользователя", example = "30", minimum = "0", maximum = "150")
    private Integer age;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Schema(description = "Дата и время создания записи", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }
}
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

/**
 * Сущность пользователя для хранения в базе данных
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    /**
     * Имя пользователя
     * ее может быть пустым, должно содержать от 2 до 100 символов
     */
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя пользователя должно содержать от 2 до 100 символов")
    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "Имя пользователя", example = "Иван Иванов", minLength = 2, maxLength = 100)
    private String name;

    /**
     * Email пользователя
     * не может быть пустым, должен быть валидным email адресом
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    @Schema(description = "Email адрес пользователя", example = "user@example.com")
    private String email;

    /**
     * Возраст пользователя
     * должен быть в диапазоне от 0 до 150 лет
     */
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Max(value = 150, message = "Возраст не может превышать 150 лет")
    @Column(name = "age", nullable = false)
    @Schema(description = "Возраст пользователя", example = "30", minimum = "0", maximum = "150")
    private Integer age;

    /**
     * Дата и время создания записи о пользователе
     * устанавливается автоматически при создании
     */
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    @Schema(description = "Дата и время создания записи", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    /**
     * Конструктор для создания нового пользователя
     *
     * @param name имя пользователя
     * @param email email пользователя
     * @param age возраст пользователя
     */
    public UserEntity(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }
}
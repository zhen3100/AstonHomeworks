package com.evgeniy.spring.springapplication.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для обновления данных пользователя
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    /**
     * Имя пользователя
     * Не может быть пустым, должно содержать от 2 до 100 символов
     */
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    /**
     * Email пользователя
     * Не может быть пустым, должен быть валидным email адресом
     */
    @NotBlank
    @Email
    private String email;

    /**
     * Возраст пользователя
     * Должен быть в диапазоне от 0 до 150 лет
     */
    @Min(value = 0)
    @Max(value = 150)
    private Integer age;
}


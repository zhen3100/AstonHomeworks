package com.evgeniy.spring.springapplication.repository;

import com.evgeniy.spring.springapplication.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по email адресу
     *
     * @param email email адрес для поиска
     * @return Optional с пользователем если найден, иначе empty
     */
    @Operation(summary = "Найти пользователя по email", description = "Возвращает пользователя по email адресу")
    Optional<User> findByEmail(@Parameter(description = "Email адрес пользователя") String email);

    /**
     * Проверяет существование пользователя с указанным email
     *
     * @param email email адрес для проверки
     * @return true если пользователь существует, иначе false
     */
    @Operation(summary = "Проверить существование пользователя по email", description = "Проверяет существует ли пользователь с указанным email")
    boolean existsByEmail(@Parameter(description = "Email адрес для проверки") String email);
}
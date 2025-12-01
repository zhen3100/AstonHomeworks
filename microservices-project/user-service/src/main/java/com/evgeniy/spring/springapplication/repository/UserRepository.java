package com.evgeniy.spring.springapplication.repository;

import com.evgeniy.spring.springapplication.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Operation(summary = "Найти пользователя по email", description = "Возвращает пользователя по email адресу")
    Optional<UserEntity> findByEmail(String email);

    @Operation(summary = "Проверить существование пользователя по email", description = "Проверяет существует ли пользователь с указанным email")
    boolean existsByEmail(String email);
}

package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Сервисный интерфейс для управления сущностями User
 * <p>Этот интерфейс определяет слой бизнес-логики для управления пользователями, предоставляя методы
 * для создания, получения, обновления, удаления пользователей
 */
@Tag(name = "Сервис пользователей", description = "Интерфейс сервиса для операций с пользователями")
public interface UserService {

    /**
     * Создает нового пользователя на основе предоставленных данных.
     *
     * @param request объект с данными для создания пользователя; не должен быть null
     * @return созданный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным email уже существует
     *                                  или данные не прошли валидацию
     * @see CreateUserRequest
     * @see UserResponse
     */
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя в системе")
    UserResponse createUser(
            @Parameter(description = "Запрос на создание пользователя", required = true)
            CreateUserRequest request);

    /**
     * Получает пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя; должен быть положительным числом
     * @return найденный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным ID не найден
     * @see UserResponse
     */
    @Operation(summary = "Получить пользователя по ID", description = "Находит пользователя по идентификатору")
    UserResponse getUserById(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            Long id);

    /**
     * Получает пользователя по его email адресу.
     *
     * @param email email адрес пользователя для поиска; не должен быть null или пустым
     * @return найденный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным email не найден
     *                                  или email имеет неверный формат
     * @see UserResponse
     */
    @Operation(summary = "Получить пользователя по email", description = "Находит пользователя по email адресу")
    UserResponse getUserByEmail(
            @Parameter(description = "Email адрес пользователя", required = true, example = "user@example.com")
            String email);

    /**
     * Получает список всех пользователей в системе.
     *
     * @return список всех пользователей в формате DTO; никогда не null, но может быть пустым
     * @see UserResponse
     */
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей системы")
    List<UserResponse> getAllUsers();

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param id      уникальный идентификатор обновляемого пользователя; должен быть положительным числом
     * @param request объект с обновленными данными пользователя; не должен быть null
     * @return обновленный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным ID не найден,
     *                                  новый email уже используется другим пользователем
     *                                  или данные не прошли валидацию
     * @see UpdateUserRequest
     * @see UserResponse
     */
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные существующего пользователя")
    UserResponse updateUser(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            Long id,
            @Parameter(description = "Запрос на обновление пользователя", required = true)
            UpdateUserRequest request);

    /**
     * Удаляет пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор удаляемого пользователя; должен быть положительным числом
     * @throws IllegalArgumentException если пользователь с указанным ID не найден
     */
    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по идентификатору")
    void deleteUserById(
            @Parameter(description = "ID пользователя для удаления", required = true, example = "1")
            Long id);

    /**
     * Удаляет пользователя по его email адресу.
     *
     * @param email email адрес удаляемого пользователя; не должен быть null или пустым
     * @throws IllegalArgumentException если пользователь с указанным email не найден
     *                                  или email имеет неверный формат
     */
    @Operation(summary = "Удалить пользователя по email", description = "Удаляет пользователя по email адресу")
    void deleteUserByEmail(
            @Parameter(description = "Email адрес пользователя для удаления", required = true, example = "user@example.com")
            String email);
}
package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;

import java.util.List;

/**
 * Сервисный интерфейс для управления сущностями User
 * <p>Этот интерфейс определяет слой бизнес-логики для управления пользователями, предоставляя методы
 * для создания, получения, обновления, удаления пользователей
 */
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
    UserResponse createUser(CreateUserRequest request);

    /**
     * Получает пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя; должен быть положительным числом
     * @return найденный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным ID не найден
     * @see UserResponse
     */
    UserResponse getUserById(Long id);

    /**
     * Получает пользователя по его email адресу.
     *
     * @param email email адрес пользователя для поиска; не должен быть null или пустым
     * @return найденный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным email не найден
     *                                  или email имеет неверный формат
     * @see UserResponse
     */
    UserResponse getUserByEmail(String email);

    /**
     * Получает список всех пользователей в системе.
     *
     * @return список всех пользователей в формате DTO; никогда не null, но может быть пустым
     * @see UserResponse
     */
    List<UserResponse> getAllUsers();

    /**
     * Обновляет данные существующего пользователя.
     *
     * @param id уникальный идентификатор обновляемого пользователя; должен быть положительным числом
     * @param request объект с обновленными данными пользователя; не должен быть null
     * @return обновленный пользователь в формате DTO
     * @throws IllegalArgumentException если пользователь с указанным ID не найден,
     *                                  новый email уже используется другим пользователем
     *                                  или данные не прошли валидацию
     * @see UpdateUserRequest
     * @see UserResponse
     */
    UserResponse updateUser(Long id, UpdateUserRequest request);

    /**
     * Удаляет пользователя по его уникальному идентификатору.
     *
     * @param id уникальный идентификатор удаляемого пользователя; должен быть положительным числом
     * @throws IllegalArgumentException если пользователь с указанным ID не найден
     */
    void deleteUserById(Long id);

    /**
     * Удаляет пользователя по его email адресу.
     *
     * @param email email адрес удаляемого пользователя; не должен быть null или пустым
     * @throws IllegalArgumentException если пользователь с указанным email не найден
     *                                  или email имеет неверный формат
     */
    void deleteUserByEmail(String email);
}

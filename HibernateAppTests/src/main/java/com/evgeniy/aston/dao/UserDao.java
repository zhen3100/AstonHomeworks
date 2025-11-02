package com.evgeniy.aston.dao;

import com.evgeniy.aston.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object интерфейс для управления сущностями User в базе данных.
 *
 * <p>Этот интерфейс определяет контракт для всех операций доступа к данным, связанных с сущностями User,
 * включая базовые CRUD операции
 */
public interface UserDao {

    /**
     * Сохраняет новую сущность User в базе данных
     *
     * @param entity сущность User для сохранения; не должна быть null
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    void save(User entity);

    /**
     * Обновляет существующую сущность User в базе данных
     *
     * @param entity сущность User для обновления; не должна быть null
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    void update(User entity);

    /**
     * Удаляет существующую сущность User из базы данных по email
     *
     * @param email адрес электронной почты пользователя для удаления; не должен быть null или пустым
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    void deleteByEmail(String email);

    /**
     * Удаляет существующую сущность User из базы данных по id
     *
     * @param id уникальный идентификатор пользователя для удаления; не должен быть null или пустым
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    void deleteById(Long id);

    /**
     * Получает сущность User по ее уникальному идентификатору.
     *
     * @param id уникальный идентификатор пользователя для получения; не должен быть null или пустым
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    Optional<User> findById(Long id);

    /**
     * Получает сущность User по ее email.
     *
     * @param email адрес электронной почты пользователя для получения; не должен быть null или пустым
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    Optional<User> findByEmail(String email);

    /**
     * Получает все сущности User из базы данных
     *
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    List<User> findAll();

    /**
     * Проверяет, существует ли сущность User с заданным адресом электронной почты.
     *
     * @param email адрес электронной почты для проверки; не должен быть null или пустым
     * @return true если User существует с заданным email, false в противном случае
     * @throws RuntimeException если происходит ошибка доступа к базе данных
     */
    boolean existsByEmail(String email);
}
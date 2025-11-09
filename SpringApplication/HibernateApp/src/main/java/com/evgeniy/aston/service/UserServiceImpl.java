package com.evgeniy.aston.service;

import com.evgeniy.aston.dao.UserDao;
import com.evgeniy.aston.dao.UserDaoImpl;
import com.evgeniy.aston.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public User createUser(String name, String email, Integer age) {
        logger.info("Создание нового пользователя: {}", email);

        validateUserData(name, email, age);

        if (userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с email " + email + " уже существует");
        }

        User user = new User(name, email, age);
        userDao.save(user);
        return user;
    }

    @Override
    public User updateUser(Long id, String name, String email, Integer age) {
        logger.info("Обновление пользователя: {}", id);

        validateUserData(name, email, age);

        User user = userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));

        // Проверяем, не используется ли email другим пользователем
        if (!user.getEmail().equals(email) && userDao.existsByEmail(email)) {
            throw new IllegalArgumentException("Email " + email + " уже используется другим пользователем");
        }

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        userDao.update(user);
        return user;
    }

    /**
     * Проверяет валидность адреса электронной почты.
     *
     * <p>Выполняет базовую проверку формата email. Проверяет наличие символа '@' в строке.
     *
     * @param email адрес электронной почты для проверки; должен быть не null
     * @throws IllegalArgumentException если email не содержит символ '@'
     */
    private void validateUserData(String email) {
        // Простая валидация email
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Неверный формат email");
        }
    }

    /**
     * Проверяет валидность данных пользователя.
     *
     * <p>Выполняет комплексную проверку всех основных полей пользователя:
     * - Проверяет что имя не пустое
     * - Проверяет что email не пустой и имеет правильный формат
     * - Проверяет что возраст в допустимом диапазоне
     *
     * @param name  имя пользователя; не должно быть null или пустым
     * @param email адрес электронной почты; не должен быть null или пустым, должен содержать '@'
     * @param age   возраст пользователя; не должен быть null, должен быть в диапазоне от 0 до 150
     * @throws IllegalArgumentException если любой из параметров не проходит валидацию
     */
    private void validateUserData(String name, String email, Integer age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (age == null || age < 0 || age > 150) {
            throw new IllegalArgumentException("Возраст должен быть от 0 до 150");
        }
        // Простая валидация email
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Неверный формат email");
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        User user = userDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));
        logger.info("Получение пользователя по id: {}", id);
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserByEmail(String email) {
        User user = userDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + email + " не найден"));
        validateUserData(email);
        logger.info("Получение пользователя по email: {}", email);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Получение всех пользователей");
        return userDao.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        logger.info("Удаление пользователя по id: {}", id);
        userDao.deleteById(id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        logger.info("Удаление пользователя по email: {}", email);
        userDao.deleteByEmail(email);
    }
}

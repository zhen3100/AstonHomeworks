package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.entity.User;
import com.evgeniy.spring.springapplication.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления пользователями
 */
@Service
@Transactional
@Tag(name = "Реализация сервиса пользователей", description = "Реализация бизнес-логики для операций с пользователями")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя после проверки уникальности email")
    public UserResponse createUser(
            @Parameter(description = "Данные для создания пользователя")
            CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        User user = new User(request.getName(), request.getEmail(), request.getAge());
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    @Operation(summary = "Получить пользователя по ID", description = "Находит пользователя по ID или выбрасывает исключение если не найден")
    public UserResponse getUserById(
            @Parameter(description = "ID пользователя")
            Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));
        return convertToResponse(user);
    }

    @Override
    @Operation(summary = "Получить пользователя по email", description = "Находит пользователя по email или выбрасывает исключение если не найден")
    public UserResponse getUserByEmail(
            @Parameter(description = "Email пользователя")
            String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + email + " не найден"));
        return convertToResponse(user);
    }

    @Override
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей в системе")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя после проверки уникальности email")
    public UserResponse updateUser(
            @Parameter(description = "ID пользователя")
            Long id,
            @Parameter(description = "Обновленные данные пользователя")
            UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));

        // Проверяем, не используется ли email другим пользователем
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " уже используется другим пользователем");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    @Override
    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по ID после проверки существования")
    public void deleteUserById(
            @Parameter(description = "ID пользователя для удаления")
            Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Пользователь не найден с таким id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Удалить пользователя по email", description = "Удаляет пользователя по email после проверки существования")
    public void deleteUserByEmail(
            @Parameter(description = "Email пользователя для удаления")
            String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким email: " + email));
        userRepository.delete(user);
    }

    /**
     * Преобразует сущность User в DTO UserResponse
     *
     * @param user сущность пользователя
     * @return DTO ответа пользователя
     */
    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}
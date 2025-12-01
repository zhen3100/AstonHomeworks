package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.entity.UserEntity;
import com.evgeniy.spring.springapplication.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@Tag(name = "Реализация сервиса пользователей", description = "Реализация бизнес-логики для операций с пользователями")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, KafkaProducerService kafkaProducerService, CircuitBreakerFactory circuitBreakerFactory) {
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }


    @Override
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя после проверки уникальности email")
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        UserEntity userEntity = new UserEntity(request.getName(), request.getEmail(), request.getAge());
        UserEntity savedUserEntity = userRepository.save(userEntity);

        // Отправка события в Kafka с Circuit Breaker
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafkaProducer");
        circuitBreaker.run(() -> {
            kafkaProducerService.sendUserCreatedEvent(
                    savedUserEntity.getEmail(),
                    savedUserEntity.getName(),
                    savedUserEntity.getId()
            );
            return null;
        }, throwable -> {
            log.warn("Failed to send Kafka event for user creation: {}", savedUserEntity.getEmail(), throwable);
            return null; // Продолжаем работу даже если Kafka недоступна
        });

        return convertToResponse(savedUserEntity);
    }

    @Override
    @Operation(summary = "Получить пользователя по ID", description = "Находит пользователя по ID или выбрасывает исключение если не найден")
    public UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));
        return convertToResponse(userEntity);
    }

    @Override
    @Operation(summary = "Получить пользователя по email", description = "Находит пользователя по email или выбрасывает исключение если не найден")
    public UserResponse getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + email + " не найден"));
        return convertToResponse(userEntity);
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
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));

        // Проверяем, не используется ли email другим пользователем
        if (!userEntity.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " уже используется другим пользователем");
        }

        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        userEntity.setAge(request.getAge());
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        return convertToResponse(updatedUserEntity);
    }

    @Override
    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по ID после проверки существования")
    public void deleteUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким id:" + id));
        String email = userEntity.getEmail();
        String name = userEntity.getName();

        userRepository.deleteById(id);

        // Отправка события в Kafka с Circuit Breaker
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("kafkaProducer");
        circuitBreaker.run(() -> {
            kafkaProducerService.sendUserDeletedEvent(email, name, id);
            return null;
        }, throwable -> {
            log.warn("Failed to send Kafka event for user deletion: {}", email, throwable);
            return null;
        });
    }

    @Override
    @Operation(summary = "Удалить пользователя по email", description = "Удаляет пользователя по email после проверки существования")
    public void deleteUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким email: " + email));

        Long id = userEntity.getId();
        String name = userEntity.getName();
        userRepository.delete(userEntity);

        kafkaProducerService.sendUserDeletedEvent(email, name, id);
    }

    private UserResponse convertToResponse(UserEntity userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getAge(),
                userEntity.getCreatedAt()
        );
    }
}
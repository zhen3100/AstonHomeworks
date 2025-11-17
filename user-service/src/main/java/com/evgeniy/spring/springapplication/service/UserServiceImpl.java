package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.entity.User;
import com.evgeniy.spring.springapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }


    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        User user = new User(request.getName(), request.getEmail(), request.getAge());
        User savedUser = userRepository.save(user);

        kafkaProducerService.sendUserCreatedEvent(
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getId()
        );

        return convertToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));
        return convertToResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + email + " не найден"));
        return convertToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
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
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким id:" + id));
        String email = user.getEmail();
        String name = user.getName();

        userRepository.deleteById(id);
        kafkaProducerService.sendUserDeletedEvent(email, name, id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким email: " + email));

        Long id = user.getId();
        String name = user.getName();
        userRepository.delete(user);

        kafkaProducerService.sendUserDeletedEvent(email, name, id);
    }

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
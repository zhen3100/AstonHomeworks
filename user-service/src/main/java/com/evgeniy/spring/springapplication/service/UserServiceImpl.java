package com.evgeniy.spring.springapplication.service;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.entity.UserEntity;
import com.evgeniy.spring.springapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
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

        UserEntity userEntity = new UserEntity(request.getName(), request.getEmail(), request.getAge());
        UserEntity savedUserEntity = userRepository.save(userEntity);

        kafkaProducerService.sendUserCreatedEvent(
                savedUserEntity.getEmail(),
                savedUserEntity.getName(),
                savedUserEntity.getId()
        );

        return convertToResponse(savedUserEntity);
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id: " + id + " не найден"));
        return convertToResponse(userEntity);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с email: " + email + " не найден"));
        return convertToResponse(userEntity);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
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
    public void deleteUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден с таким id:" + id));
        String email = userEntity.getEmail();
        String name = userEntity.getName();

        userRepository.deleteById(id);
        kafkaProducerService.sendUserDeletedEvent(email, name, id);
    }

    @Override
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
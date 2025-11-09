package com.evgeniy.aston.service;

import com.evgeniy.aston.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class ServiceTest extends BaseServiceTest {

    @Test
    void createUser_WithValidData_ShouldCreateUserSuccessfully() {
        // Given
        String name = "Иван Иванов";
        String email = "ivan@test.ru";
        Integer age = 25;

        Mockito.when(userDao.existsByEmail(email)).thenReturn(false);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // When
        User result = userService.createUser(name, email, age);

        // Then
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(age, result.getAge());

        // Проверяем, что save был вызван с правильным пользователем
        verify(userDao).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(name, capturedUser.getName());
        assertEquals(email, capturedUser.getEmail());
        assertEquals(age, capturedUser.getAge());
        verify(userDao).existsByEmail(email);
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowException() {
        // Given
        String name = "Иван Иванов";
        String email = "ivan@test.ru";
        Integer age = 25;

        Mockito.when(userDao.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(name, email, age)
        );

        assertTrue(exception.getMessage().contains("уже существует"));
        verify(userDao).existsByEmail(email);
        verify(userDao, Mockito.never()).save(any(User.class));
    }

    @Test
    void createOrUpdateUser_WithInvalidUserName_ShouldThrowException() {
        Long id = 1L;
        String invalidName = "";
        String email = "test@test.ru";
        Integer age = 25;

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(invalidName, email, age),
                "Имя должно быть пустое"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(id, invalidName, email, age),
                "Имя должно быть пустое"
        );

        verify(userDao, Mockito.never()).existsByEmail(Mockito.anyString());
        verify(userDao, Mockito.never()).update(any(User.class));
        verify(userDao, Mockito.never()).save(any(User.class));
    }

    @Test
    void createOrUpdateUser_WithInvalidEmail_ShouldThrowException() {
        // Given
        Long id = 1L;
        String invalidName = "Ivan Ivanov";
        String email = "test";
        Integer age = 25;

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(invalidName, email, age),
                "Email должен быть пустым или в неверном формате"
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(id, invalidName, email, age),
                "Email должен быть пустым или в неверном формате"
        );

        verify(userDao, Mockito.never()).existsByEmail(Mockito.anyString());
        verify(userDao, Mockito.never()).update(any(User.class));
        verify(userDao, Mockito.never()).save(any(User.class));
    }

    @Test
    void createOrUpdateUser_WithInvalidAge_ShouldThrowException() {
        Long id = 1L;
        String invalidName = "Ivan Ivanov";
        String email = "test@test.ru";
        Integer age = -99;

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(invalidName, email, age),
                "Возраст должен быть >150 либо <0"
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(id, invalidName, email, age),
                "Возраст должен быть >150 либо <0"
        );

        verify(userDao, Mockito.never()).existsByEmail(Mockito.anyString());
        verify(userDao, Mockito.never()).save(any(User.class));
        verify(userDao, Mockito.never()).update(any(User.class));
    }

    @Test
    void updateUser_WithValidData_ShouldUpdateUserSuccessfully() {
        // Given
        Long userId = 1L;
        String newName = "Новое Имя";
        String newEmail = "new@test.ru";
        Integer newAge = 30;

        User existingUser = createTestUser(userId, "Старое Имя", "old@test.ru", 25);

        // When
        Mockito.when(userDao.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userDao.existsByEmail(newEmail)).thenReturn(false);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User result = userService.updateUser(userId, newName, newEmail, newAge);

        // Then
        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(newEmail, result.getEmail());
        assertEquals(newAge, result.getAge());

        // Проверяем, что update был вызван с правильным пользователем
        verify(userDao).update(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(newName, capturedUser.getName());
        assertEquals(newEmail, capturedUser.getEmail());
        assertEquals(newAge, capturedUser.getAge());
        verify(userDao).findById(userId);
        verify(userDao).existsByEmail(newEmail);
    }

    @Test
    void updateUser_WithSameEmail_ShouldUpdateUserSuccessfully() {
        // Given
        Long userId = 1L;
        String newName = "Новое Имя";
        String sameEmail = "new@test.ru";
        Integer newAge = 30;

        User existingUser = createTestUser(userId, "Старое Имя", sameEmail, 25);

        // When
        Mockito.when(userDao.findById(userId)).thenReturn(Optional.of(existingUser));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.updateUser(userId, newName, sameEmail, newAge);

        // Then
        // Проверяем, что update был вызван с правильными данными
        verify(userDao).update(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(newName, capturedUser.getName());
        assertEquals(sameEmail, capturedUser.getEmail());
        assertEquals(newAge, capturedUser.getAge());

        // Ключевая проверка: existsByEmail не должен вызываться
        verify(userDao, Mockito.never()).existsByEmail(sameEmail);
        verify(userDao).findById(userId);
    }

    @Test
    void updateUser_WithNonExistingUser_ShouldThrowException() {
        // Given
        Long userId = 999L;

        Mockito.when(userDao.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(userId, "Новое Имя", "new@test.ru", 30)
        );

        assertTrue(exception.getMessage().contains("не найден"));
        verify(userDao).findById(userId);
        verify(userDao, Mockito.never()).existsByEmail(Mockito.anyString());
        verify(userDao, Mockito.never()).update(any(User.class));
    }

    @Test
    void updateUser_WithDuplicateEmail_ShouldThrowException() {
        // Given
        Long userId = 1L;
        String newEmail = "duplicate@test.ru";

        User existingUser = createTestUser(userId, "Иван Иванов", "old@test.ru", 25);

        Mockito.when(userDao.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userDao.existsByEmail(newEmail)).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(userId, "Новое Имя", newEmail, 30)
        );

        assertTrue(exception.getMessage().contains("уже используется"));
        verify(userDao).findById(userId);
        verify(userDao).existsByEmail(newEmail);
        verify(userDao, Mockito.never()).update(any(User.class));
    }

    @Test
    void whenGetUserByEmail_thenUserReturned() {
        // Given
        String email = "ivan@test.ru";
        User expectedUser = createTestUser(1L, "Иван Иванов", email, 25);

        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<User> result = userService.getUserByEmail(email);

        // Then
        assertTrue(result.isPresent(), "Пользователь должен быть найден");
        assertEquals(expectedUser, result.get());
        assertEquals(email, result.get().getEmail());
        assertEquals("Иван Иванов", result.get().getName());
        verify(userDao).findByEmail(email);
    }

    @Test
    void deleteUserByEmail_ShouldCallDaoMethod() {
        // Given
        String email = "delete@test.ru";

        // When
        userService.deleteUserByEmail(email);

        // Then
        verify(userDao).deleteByEmail(email);
    }

    @Test
    void deleteUserById_ShouldCallDaoMethod() {
        // Given
        Long userId = 1L;

        // When
        userService.deleteUserById(userId);

        // Then
        verify(userDao).deleteById(userId);
    }

    @Test
    void getUserByEmail_WithNonExistingUser_ShouldReturnEmpty() {
        String email = "nonexisting@mail.ru";

        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserByEmail(email)
        );

        assertTrue(exception.getMessage().contains("не найден"));
        verify(userDao).findByEmail(email);
    }

    @Test
    void whenGetUserById_thenUserReturned() {
        Long userId = 1L;
        User expectedUser = createTestUser(userId, "Иван Иванов", "ivan@test.ru", 25);

        Mockito.when(userDao.findById(userId)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<User> result = userService.getUserById(userId);

        // Then
        assertTrue(result.isPresent(), "Пользователь должен быть найден");
        assertEquals(expectedUser, result.get());
        assertEquals(userId, result.get().getId());
        assertEquals("Иван Иванов", result.get().getName());
        verify(userDao).findById(userId);
    }

    @Test
    void whenGetUserById_WithNonExistingUser_ShouldReturnEmpty() {
        Long userId = 999L;

        Mockito.when(userDao.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(userId)
        );

        assertTrue(exception.getMessage().contains("не найден"));
        verify(userDao).findById(userId);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<User> expectedUsers = Arrays.asList(
                createTestUser(1L, "Иван Иванов", "ivan@test.ru", 25),
                createTestUser(2L, "Мария Петрова", "maria@test.ru", 30)
        );

        Mockito.when(userDao.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userDao).findAll();
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Given
        Mockito.when(userDao.findAll()).thenReturn(List.of());

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertTrue(result.isEmpty());
        verify(userDao).findAll();
    }
}
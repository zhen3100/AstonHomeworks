package com.evgeniy.aston.dao;

import com.evgeniy.aston.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserDaoIntegrationTest extends BaseIntegrationTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.userDao = new UserDaoImpl();
    }

    @Test
    void shouldSaveUserSuccessfully() {
        User user = new User("Ivan Ivanov", "ivan@123test.ru", 23);

        userDao.save(user);

        assertNotNull(user.getId());
        assertEquals("Ivan Ivanov", user.getName());
        assertEquals("ivan@123test.ru", user.getEmail());
        assertEquals(23, user.getAge());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        User user = new User("Старое Имя", "old@test.ru", 40);
        userDao.save(user);
        Long userId = user.getId();

        // When
        user.setName("Новое Имя");
        user.setEmail("new@test.ru");
        user.setAge(45);
        userDao.update(user);

        // Then
        Optional<User> updatedUser = userDao.findById(userId);
        assertTrue(updatedUser.isPresent());
        assertEquals("Новое Имя", updatedUser.get().getName());
        assertEquals("new@test.ru", updatedUser.get().getEmail());
        assertEquals(45, updatedUser.get().getAge());
    }

    @Test
    void shouldDeleteUserByIdSuccessfully() {
        // Given
        User user = new User("Ivan Ivanov", "ivan@123test.ru", 23);
        userDao.save(user);
        userDao.deleteById(user.getId());

        // Then
        Optional<User> updatedUser = userDao.findById(user.getId());
        assertFalse(updatedUser.isPresent());
    }

    @Test
    void shouldDeleteUserByEmailSuccessfully() {
        // Given
        User user = new User("Ivan Ivanov", "ivan@123test.ru", 23);
        userDao.save(user);
        userDao.deleteByEmail(user.getEmail());

        // Then
        Optional<User> updatedUser = userDao.findByEmail(user.getEmail());
        assertFalse(updatedUser.isPresent());
    }

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = new User("Мария Петрова", "maria@test.ru", 30);
        userDao.save(user);
        String email = user.getEmail();

        // When
        Optional<User> foundUser = userDao.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("Мария Петрова", foundUser.get().getName());
        assertEquals("maria@test.ru", foundUser.get().getEmail());
        assertEquals(30, foundUser.get().getAge());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // When
        Optional<User> foundUser = userDao.findByEmail("nonexistent@test.ru");

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldFindUserById() {
        // Given
        User user = new User("Мария Петрова", "maria@test.ru", 30);
        userDao.save(user);
        Long userId = user.getId();

        // When
        Optional<User> foundUser = userDao.findById(userId);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("Мария Петрова", foundUser.get().getName());
        assertEquals("maria@test.ru", foundUser.get().getEmail());
        assertEquals(30, foundUser.get().getAge());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        // When
        Optional<User> foundUser = userDao.findById((long) -10000000);

        // Then
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldFindAll() {
        // Given
        userDao.save(new User("User1", "user1@test.ru", 20));
        userDao.save(new User("User2", "user2@test.ru", 25));
        userDao.save(new User("User3", "user3@test.ru", 30));

        // When
        List<User> users = userDao.findAll();

        // Then
        assertEquals(3, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@test.ru")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@test.ru")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user3@test.ru")));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        // When
        List<User> users = userDao.findAll();

        // Then
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldExistsByEmail() {
        User user = new User("Проверка Email", "check@test.ru", 60);
        userDao.save(user);

        // When & Then
        assertTrue(userDao.existsByEmail("check@test.ru"));
        assertFalse(userDao.existsByEmail("notexists@test.ru"));
    }
}

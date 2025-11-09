package com.evgeniy.aston.service;


import com.evgeniy.aston.dao.UserDao;
import com.evgeniy.aston.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {

    @Mock
    protected UserDao userDao;

    @InjectMocks
    protected UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);
    }

    protected User createTestUser(Long id, String name, String email, Integer age) {
        User user = new User(name, email, age);
        user.setId(id);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

}

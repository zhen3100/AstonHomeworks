package com.evgeniy.spring.springapplication;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(topics = "user-events", partitions = 1)
@DirtiesContext
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUser_ShouldPersistUserAndSendKafkaEvent() {
        CreateUserRequest request = new CreateUserRequest("Integration Test", "integration@test.com", 30);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Integration Test", response.getName());
        assertEquals("integration@test.com", response.getEmail());
    }

    @Test
    void createUserWithDuplicateEmail_ShouldThrowException() {
        CreateUserRequest request1 = new CreateUserRequest("User1", "duplicate@test.com", 25);
        CreateUserRequest request2 = new CreateUserRequest("User2", "duplicate@test.com", 30);

        userService.createUser(request1);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request2);
        });
    }
}

package com.evgeniy.spring.springapplication;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.controller.UserController;
import com.evgeniy.spring.springapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    UserService userService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void createUser_WithValidData_ShouldReturnCreated() throws Exception {
        CreateUserRequest request = new CreateUserRequest("Ivan Ivanov", "ivan@123test.ru", 23);
        UserResponse response = new UserResponse(1L,"Ivan Ivanov", "ivan@123test.ru", 23, LocalDateTime.now());

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan@123test.ru"))
                .andExpect(jsonPath("$.age").value(23));
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "invalid-email", -5);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUser() throws Exception {
        UserResponse response = new UserResponse(1L,"Ivan Ivanov", "ivan@123test.ru", 23, LocalDateTime.now());

        when(userService.getUserById(1L)).thenReturn(response);

        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$.email").value("ivan@123test.ru"));
    }

    @Test
    void getUserById_WithNonExistingUser_ShouldReturnBadRequest() throws Exception {
        when(userService.getUserById(999L)).thenThrow(new IllegalArgumentException("User not found"));

        mvc.perform(get("/api/users/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        List<UserResponse> users = Arrays.asList(
                new UserResponse(1L,"Ivan Ivanov", "ivan@123test.ru", 23, LocalDateTime.now()),
                new UserResponse(2L,"Evgeniy Volkov", "evg@gmail.com", 25, LocalDateTime.now())
        );

        when(userService.getAllUsers()).thenReturn(users);

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ivan Ivanov"))
                .andExpect(jsonPath("$[1].name").value("Evgeniy Volkov"));
    }

    @Test
    void updateUser_WithNonExistingUser_ShouldReturnBadRequest() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("New Name", "new@test.com", 30);

        when(userService.updateUser(eq(999L), any(UpdateUserRequest.class)))
                .thenThrow(new IllegalArgumentException("User not found with id: 999"));

        mvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("Ivan Ivanov", "duplicate@test.com", 30);

        when(userService.updateUser(eq(1L), any(UpdateUserRequest.class)))
                .thenThrow(new IllegalArgumentException("Email" + request.getEmail()+" is already used by another user"));

        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email" + request.getEmail()+" is already used by another user"));
    }

    @Test
    void updateUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("", "invalid-email", -5);

        mvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserById_ShouldReturnNoContent() throws Exception {
        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(1L);
    }

    @Test
    void deleteUserById_WithNonExistingUser_ShouldReturnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("User not found with id: 999"))
                .when(userService).deleteUserById(999L);

        mvc.perform(delete("/api/users/999"))
                .andExpect(status().isBadRequest());

        verify(userService).deleteUserById(999L);
    }

    @Test
    void deleteUserByEmail_ShouldReturnNoContent() throws Exception {
        mvc.perform(delete("/api/users/email/test@test.com"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserByEmail("test@test.com");
    }

    @Test
    void deleteUserByEmail_WithNonExistingEmail_ShouldReturnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException("User not found with email: none@test.com"))
                .when(userService).deleteUserByEmail("none@test.com");

        mvc.perform(delete("/api/users/email/none@test.com"))
                .andExpect(status().isBadRequest());

        verify(userService).deleteUserByEmail("none@test.com");
    }
}

package com.evgeniy.spring.springapplication.controller;

import com.evgeniy.spring.springapplication.DTO.CreateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UpdateUserRequest;
import com.evgeniy.spring.springapplication.DTO.UserResponse;
import com.evgeniy.spring.springapplication.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Управление пользователями", description = "API для управления пользователями системы")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя с предоставленными данными"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserResponse>> createUser(
            @Parameter(description = "Данные для создания пользователя", required = true)
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse user = userService.createUser(request);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        addUserLinks(resource, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по его уникальному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный ID пользователя",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> getUserById(
            @Parameter(description = "ID пользователя для поиска", required = true, example = "1")
            @PathVariable Long id) {
        UserResponse user = userService.getUserById(id);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        addUserLinks(resource, user);

        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Получить пользователя по email",
            description = "Возвращает пользователя по его email адресу"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверный формат email",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<EntityModel<UserResponse>> getUserByEmail(
            @Parameter(description = "Email адрес пользователя для поиска", required = true, example = "user@example.com")
            @PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        addUserLinks(resource, user);

        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей системы"
    )
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAllUsers() {
        List<EntityModel<UserResponse>> users = userService.getAllUsers().stream()
                .map(user -> {
                    EntityModel<UserResponse> resource = EntityModel.of(user);
                    addUserLinks(resource, user);
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResponse>> resources = CollectionModel.of(users);

        // Добавляем ссылки для коллекции
        resources.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        resources.add(linkTo(methodOn(UserController.class).createUser(new CreateUserRequest())).withRel("create"));

        return ResponseEntity.ok(resources);
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет информацию существующего пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @Parameter(description = "ID пользователя для обновления", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные пользователя", required = true)
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse user = userService.updateUser(id, request);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        addUserLinks(resource, user);

        return ResponseEntity.ok(resource);
    }

    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по его уникальному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный ID пользователя",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "ID пользователя для удаления", required = true, example = "1")
            @PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить пользователя по email",
            description = "Удаляет пользователя по его email адресу"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный формат email",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content)
    })
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(
            @Parameter(description = "Email адрес пользователя для удаления", required = true, example = "user@example.com")
            @PathVariable String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Добавляет HATEOAS ссылки к ресурсу пользователя
     */
    private void addUserLinks(EntityModel<UserResponse> resource, UserResponse user) {
        // Self link
        resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());

        // Альтернативные способы доступа к ресурсу
        resource.add(linkTo(methodOn(UserController.class).getUserByEmail(user.getEmail())).withRel("by-email"));

        // Действия с ресурсом
        resource.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), new UpdateUserRequest())).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUserById(user.getId())).withRel("delete"));

        // Навигационные ссылки
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel(IanaLinkRelations.COLLECTION));
        resource.add(linkTo(methodOn(UserController.class).createUser(new CreateUserRequest())).withRel("create-user"));
    }
}
package com.evgeniy.aston;


import com.evgeniy.aston.display.UserDisplay;
import com.evgeniy.aston.entity.User;
import com.evgeniy.aston.service.UserService;
import com.evgeniy.aston.service.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final UserService userService = new UserServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("Запуск приложения");

        try {
            UserDisplay.displayMenu();
            boolean running = true;

            while (running) {
                System.out.print("\nВведите ваш выбор: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createUser();
                        break;
                    case "2":
                        getUserById();
                        break;
                    case "3":
                        getUserByEmail();
                        break;
                    case "4":
                        getAllUsers();
                        break;
                    case "5":
                        updateUser();
                        break;
                    case "6":
                        deleteUserById();
                        break;
                    case "7":
                        deleteUserByEmail();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }

                if (running) {
                    System.out.println("\nНажмите Enter для продолжения...");
                    scanner.nextLine();
                    UserDisplay.displayMenu();
                }
            }

        } catch (Exception e) {
            logger.error("Ошибка приложения", e);
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private static void createUser() {
        System.out.println("\n--- СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ ---");
        try {
            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = userService.createUser(name, email, age);
            UserDisplay.displaySuccessMessage("Пользователь успешно создан!");
            UserDisplay.displayUserCard(user);

        } catch (NumberFormatException e) {
            UserDisplay.displayErrorMessage("Возраст должен быть числом");
        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void getUserById() {
        System.out.println("\n--- ПОЛУЧЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО ID ---");
        try {
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());

            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                System.out.println();
                UserDisplay.displayUserCard(user.get());
            } else {
                UserDisplay.displayErrorMessage("Пользователь не найден с ID: " + id);
            }
        } catch (NumberFormatException e) {
            UserDisplay.displayErrorMessage("ID должен быть числом");
        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void getAllUsers() {
        System.out.println("\n--- ПОЛУЧЕНИЕ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ ---");
        try {
            List<User> users = userService.getAllUsers();
            Collections.sort(users);
            if (users.isEmpty()) {
                UserDisplay.displayNoUsersFound();
            } else {
                System.out.println("Найдено " + users.size() + " пользователей:\n");
                UserDisplay.displayUsersList(users);
            }
        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.println("\n--- ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ---");
        try {
            System.out.print("Введите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());

            System.out.print("Введите новое имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите новый email: ");
            String email = scanner.nextLine();

            System.out.print("Введите новый возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = userService.updateUser(id, name, email, age);
            UserDisplay.displaySuccessMessage("Пользователь успешно обновлен!");
            UserDisplay.displayUserCard(user);

        } catch (NumberFormatException e) {
            UserDisplay.displayErrorMessage("ID и возраст должны быть числами");
        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void deleteUserById() {
        System.out.println("\n--- УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО ID ---");
        try {
            System.out.print("Введите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());

            // Проверяем существование пользователя перед удалением
            if (userService.getUserById(id).isPresent()) {
                userService.deleteUserById(id);
                UserDisplay.displaySuccessMessage("Пользователь успешно удален");
            } else {
                UserDisplay.displayErrorMessage("Пользователь не найден с ID: " + id);
            }
        } catch (NumberFormatException e) {
            UserDisplay.displayErrorMessage("ID должен быть числом");
        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void deleteUserByEmail() {
        System.out.println("\n--- УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО EMAIL ---");
        try {
            System.out.print("Введите email для удаления: ");
            String email = scanner.nextLine();

            // Проверяем существование email перед удалением
            if (userService.getUserByEmail(email).isPresent()) {
                userService.deleteUserByEmail(email);
                UserDisplay.displaySuccessMessage("Пользователь успешно удален");
            } else {
                UserDisplay.displayErrorMessage("Пользователь не найден с email: " + email);
            }

        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

    private static void getUserByEmail() {
        System.out.println("\n--- ПОЛУЧЕНИЕ ПОЛЬЗОВАТЕЛЯ ПО EMAIL ---");
        try {
            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                System.out.println();
                UserDisplay.displayUserCard(user.get());
            } else {
                UserDisplay.displayErrorMessage("Пользователь не найден с email: " + email);
            }

        } catch (Exception e) {
            UserDisplay.displayErrorMessage(e.getMessage());
        }
    }

}
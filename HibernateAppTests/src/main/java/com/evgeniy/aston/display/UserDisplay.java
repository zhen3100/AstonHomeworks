package com.evgeniy.aston.display;

import com.evgeniy.aston.entity.User;

import java.util.List;

/**
 * Класс для отображения интерфейса вывода
 */
public class UserDisplay {

    public UserDisplay() {

    }

    /**
     * Отображает красивую карточку пользователя с иконками.
     *
     * @param user сущность пользователя для отображения; не должна быть null
     */
    public static void displayUserCard(User user) {
        System.out.println("╔═════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-55s ║\n", "🆔 ID: " + user.getId());
        System.out.println("╠═════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-55s ║\n", "👨‍💼 " + user.getName());
        System.out.printf("║ %-55s ║\n", "📧 " + user.getEmail());
        System.out.printf("║ %-55s ║\n", "🎂 Возраст: " + user.getAge() + " лет");
        System.out.printf("║ %-55s ║\n", "📅 Создан: " +
                user.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm")));
        System.out.println("╚═════════════════════════════════════════════════════════╝");
    }

    /**
     * Отображает таблицу со списком пользователей.
     *
     * @param users список пользователей для отображения; не должен быть null
     */
    public static void displayUsersList(List<User> users) {
        System.out.println("┌─────┬────────────┬──────────────────────┬──────┬─────────────────────┐");
        System.out.println("│ №   │ Имя        │ Email                │ Возр │ Дата создания       │");
        System.out.println("├─────┼────────────┼──────────────────────┼──────┼─────────────────────┤");

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.printf("│ %-3d │ %-10s │ %-20s │ %-4d │ %-19s │\n",
                    (i + 1),
                    truncate(user.getName(), 10),
                    truncate(user.getEmail(), 20),
                    user.getAge(),
                    user.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }

        System.out.println("└─────┴────────────┴──────────────────────┴──────┴─────────────────────┘");
    }

    /**
     * Отображает сообщение о том, что пользователи не найдены.
     */
    public static void displayNoUsersFound() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-55s │\n", "📭 ПОЛЬЗОВАТЕЛИ НЕ НАЙДЕНЫ");
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }

    /**
     * Отображает сообщение об успешной операции.
     *
     * @param message текст сообщения для отображения; не должен быть null
     */
    public static void displaySuccessMessage(String message) {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-55s │\n", "✅ " + message);
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }

    /**
     * Отображает сообщение об ошибке.
     *
     * @param message текст сообщения об ошибке; не должен быть null
     */
    public static void displayErrorMessage(String message) {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-55s │\n", "❌ " + message);
        System.out.println("└─────────────────────────────────────────────────────────┘");
    }

    public static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ");
        System.out.println("=".repeat(50));
        System.out.println("1.  Создать пользователя");
        System.out.println("2.  Получить пользователя по ID");
        System.out.println("3.  Получить пользователя по Email");
        System.out.println("4.  Получить всех пользователей");
        System.out.println("5.  Обновить пользователя");
        System.out.println("6.  Удалить пользователя по ID");
        System.out.println("7.  Удалить пользователя по Email");
        System.out.println("0.  Выход");
        System.out.println("=".repeat(50));
    }

    private static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

}

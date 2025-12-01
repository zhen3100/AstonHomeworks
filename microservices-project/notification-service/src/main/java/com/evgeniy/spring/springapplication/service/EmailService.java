package com.evgeniy.spring.springapplication.service;


/**
 * Сервис для отправки электронных писем
 */
public interface EmailService {

    /**
     * Отправляет электронное письмо указанному адресату
     *
     * @param to адрес электронной почты получателя
     * @param subject тема письма
     * @param text текст письма
     * @throws RuntimeException если произошла ошибка при отправке письма
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Отправляет уведомление о создании учетной записи пользователя
     *
     * @param email адрес электронной почты пользователя
     * @param userName имя пользователя
     * @throws RuntimeException если произошла ошибка при отправке письма
     */
    void sendUserCreatedNotification(String email, String userName);

    /**
     * Отправляет уведомление об удалении учетной записи пользователя
     *
     * @param email адрес электронной почты пользователя
     * @param userName имя пользователя
     * @throws RuntimeException если произошла ошибка при отправке письма
     */
    void sendUserDeletedNotification(String email, String userName);
}

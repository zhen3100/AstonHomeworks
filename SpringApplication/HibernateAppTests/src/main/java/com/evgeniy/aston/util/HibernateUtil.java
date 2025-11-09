package com.evgeniy.aston.util;

import com.evgeniy.aston.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Создание SessionFactory из hibernate.cfg.xml
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();

        } catch (Throwable ex) {
            logger.error("Инициализация SessionFactory не удалась: {}", String.valueOf(ex));
            throw new ExceptionInInitializerError();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        logger.info("Closing Hibernate SessionFactory");
        getSessionFactory().close();
    }
}

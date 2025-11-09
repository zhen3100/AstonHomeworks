package com.evgeniy.aston.dao;

import com.evgeniy.aston.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;


public class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    protected Session openSession() {
        return com.evgeniy.aston.util.HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public void save(User entity) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
            logger.info("Пользователь успешно сохранен: {}", entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка сохранения пользователя: {}", entity, e);
            throw new RuntimeException();
        }

    }

    @Override
    public void update(User entity) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            logger.info("Пользователь успешно обновлен: {}", entity);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка обновления пользователя: {}", entity.getEmail(), e);
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteByEmail(String email) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery("DELETE from User u where u.email = :email ");
            query.setParameter("email", email);
            int deletedCount = query.executeUpdate();
            transaction.commit();
            if (deletedCount > 0) {
                logger.info("Удаление пользователя по email: {}", email);
            } else {
                logger.warn("Пользователь не найден для удаления с email: {}", email);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка удаления пользователя с email: {}", email, e);
            throw new RuntimeException();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("Пользователь успешно удален по id: {}", id);
            } else {
                logger.warn("Пользователь не найден для удаления с id: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка удаления пользователя с id: {}", id, e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = openSession()) {
            User user = session.get(User.class, id);
            logger.info("Пользователь найден по id {}: {}", id, user != null);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка поиска пользователя с id {}", id, e);
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = openSession()) {
            Query query = session.createQuery("from User u where u.email = :email ");
            query.setParameter("email", email);
            User user = (User) query.uniqueResult();
            logger.info("Пользователь найден по email {}: {}", email, user != null);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка поиска пользователя с email: {}", email, e);
            throw new RuntimeException();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.getResultList();
            logger.debug("Найдено {} пользователей", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка поиска всех пользователей", e);
            throw new RuntimeException();
        }
    }

    public boolean existsByEmail(String email) {
        try (Session session = openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE email = :email", Long.class);
            query.setParameter("email", email);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            logger.error("Ошибка проверки существования пользователя по email: {}", email, e);
            throw new RuntimeException();
        }
    }
}

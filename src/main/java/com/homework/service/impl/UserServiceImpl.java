package com.homework.service.impl;

import com.homework.exception.*;
import com.homework.model.User;
import com.homework.service.UserService;
import com.homework.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void add(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Пользователь сохранён: {}", user);
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try {
                    tx.rollback();
                }  catch (IllegalStateException ise) {
                logger.error("Невозможно откатить транзакцию: соединение уже закрыто", ise);
            } catch (Exception rollbackEx) {
                    logger.error("Ошибка при откате транзакции", rollbackEx);
                }
            }
            logger.error("Ошибка при сохранении пользователя", e);
            throw new EntitySaveException("Не удалось сохранить пользователя", e);
        }
    }

    @Override
    public void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Пользователь обновлён: {}", user);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при обновлении пользователя", e);
            throw new EntityUpdateException("Не удалось обновить пользователя", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            logger.error("Ошибка при поиске пользователя по id={}", id, e);
            throw new UserNotFoundException("Не удалось найти пользователя", e);
        }
    }

    @Override
    public void remove(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user); // просто удаляем
                tx.commit(); // и коммитим
                logger.info("Пользователь удалён: {}", user);
            } else {
                logger.warn("Пользователь с id={} не найден", id);
                throw new UserNotFoundException("Пользователь не найден для удаления", null);
            }
        } catch (Exception e) {
            if (tx != null && tx.getStatus().canRollback()) {
                tx.rollback();
            }
            logger.error("Ошибка при удалении пользователя", e);
            throw new EntityDeleteException("Не удалось удалить пользователя", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Ошибка при получении всех пользователей", e);
            throw new UserNotFoundException("Ошибка получения списка пользователей", e);
        }
    }
}

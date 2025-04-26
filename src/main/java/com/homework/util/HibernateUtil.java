package com.homework.util;

import com.homework.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private HibernateUtil() {
    }

    static {
        try {
            logger.debug("Начинаю инициализацию SessionFactory.");

            // Загрузка свойств
            Properties properties = loadHibernateProperties();
            logger.debug("Свойства Hibernate загружены: {}", properties);

            // Создание SessionFactory
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .addProperties(loadHibernateProperties()) // Если хочешь использовать .properties
                    .buildSessionFactory();
            logger.debug("SessionFactory успешно создан.");

        } catch (Throwable ex) {
            System.err.println("Ошибка при инициализации SessionFactory.");
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties loadHibernateProperties() {
        Properties properties = new Properties();
        try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
            if (input == null) {
                logger.warn("Не удалось найти hibernate.properties");
                return properties;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            logger.debug("Закрытие SessionFactory.");
            sessionFactory.close();
        }
    }
}

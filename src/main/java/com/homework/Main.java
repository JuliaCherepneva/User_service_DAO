package com.homework;

import com.homework.model.User;
import com.homework.service.UserService;
import com.homework.service.impl.UserServiceImpl;
import com.homework.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        boolean exit = false;
        try {
            logger.info("Запуск приложения.");
            while (!exit) {
                printMenu();
                String input = scanner.nextLine();
                switch (input) {
                    case "1" -> createUser();
                    case "2" -> findUserById();
                    case "3" -> findAllUsers();
                    case "4" -> updateUser();
                    case "5" -> deleteUser();
                    case "6" -> exit = true;
                    default -> System.out.println("Неверный выбор. Повторите попытку.");
                }
            }
        } catch (Exception e) {
            logger.error("Произошла ошибка в приложении", e);
        } finally {
            scanner.close();
            HibernateUtil.shutdown();
            logger.info("Программа завершена.");
        }
    }

    private static void printMenu() {
        System.out.println("""
                
                === Меню ===
                1. Создать пользователя
                2. Найти пользователя по ID
                3. Показать всех пользователей
                4. Обновить пользователя
                5. Удалить пользователя
                6. Выйти
                Выберите опцию:
                """);
    }

    private static void createUser() {
        logger.debug("Создание нового пользователя.");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userService.add(user);
        logger.debug("Пользователь {} был создан.", user);
    }

    private static void findUserById() {
        logger.debug("Поиск пользователя по ID.");
        System.out.print("Введите ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> userOpt = userService.findById(id);
        userOpt.ifPresentOrElse(
                user -> logger.debug("Найден пользователь: {}", user),
                () -> logger.warn("Пользователь с ID {} не найден.", id)
        );
    }

    private static void findAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            System.out.println("Пользователь не найден.");
            return;
        }

        User user = userOpt.get();

        System.out.print("Новое имя (текущее: " + user.getName() + "): ");
        user.setName(scanner.nextLine());

        System.out.print("Новый email (текущий: " + user.getEmail() + "): ");
        user.setEmail(scanner.nextLine());

        System.out.print("Новый возраст (текущий: " + user.getAge() + "): ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        userService.update(user);
        System.out.println("Пользователь обновлён.");
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userService.remove(id);
        System.out.println("Пользователь удалён.");
    }
}
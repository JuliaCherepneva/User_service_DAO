package com.homework.service;

import com.homework.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
void add(User user);
Optional<User> findById (Long id);
void update (User user);
void remove (Long id);
List<User> findAll();

/*
User findUserByAge (int age);
User findUserByCreatedAt (LocalDate createdAt);
User findUserByName (String name);
User findUserByEmail (String email);


 void create(User user);
    User read(Long id);
    void update(User user);
    void delete(User user);
    List<User> getAll();
 */

}

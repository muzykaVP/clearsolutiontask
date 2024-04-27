package com.example.clearsolutiontask.service;

import com.example.clearsolutiontask.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User findById(Long id);
    User create(User user);

    User update(Long id, User user);
    void deleteById(Long id);
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);

}
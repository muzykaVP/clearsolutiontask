package com.example.clearsolutiontask.service.impl;

import com.example.clearsolutiontask.exception.DataProcessingException;
import com.example.clearsolutiontask.exception.DuplicateKeyException;
import com.example.clearsolutiontask.model.User;
import com.example.clearsolutiontask.repository.UserRepository;
import com.example.clearsolutiontask.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new DataProcessingException(String.format("User with id: %s not found", id)));
    }

    @Override
    public User create(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException(String.format("User with email: \"%s\" already exist", user.getEmail()));
        }
        return user;
    }

    @Override
    public User update(Long id, User user) {
        findById(id);
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new DataProcessingException("Date \"from\" can't exceed the date \"to\"");
        }
        return userRepository.findUsersByBirthDateBetween(from, to);
    }

}

package com.example.clearsolutiontask.repository;

import com.example.clearsolutiontask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);
}
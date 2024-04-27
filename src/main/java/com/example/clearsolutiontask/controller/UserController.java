package com.example.clearsolutiontask.controller;


import com.example.clearsolutiontask.dto.UserRequestDTO;
import com.example.clearsolutiontask.dto.UserResponseDTO;
import com.example.clearsolutiontask.mapper.UserMapper;
import com.example.clearsolutiontask.model.User;
import com.example.clearsolutiontask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@RequestParam Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserResponseDTO(user));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userDTO) {
        User user = userService.create(UserMapper.INSTANCE.userRequestDTOToUser(userDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.INSTANCE.userToUserResponseDTO(user));
    }

    @PutMapping(value = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> update(@RequestParam Long id, @RequestBody @Valid UserRequestDTO userDTO) {
        User updatedUser = userService.update(id, UserMapper.INSTANCE.userRequestDTOToUser(userDTO));
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserResponseDTO(updatedUser));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Object> delete(@RequestParam Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search-by-date")
    public ResponseEntity<List<UserResponseDTO>> findUsersByBirthDateBetween(@RequestParam(name = "dateFrom") LocalDate from,
                                             @RequestParam(name = "dateTo") LocalDate to) {
        return ResponseEntity.ok(userService.findUsersByBirthDateBetween(from, to)
                .stream()
                .map(UserMapper.INSTANCE::userToUserResponseDTO)
                .toList());
    }
}

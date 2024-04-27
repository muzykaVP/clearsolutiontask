package com.example.clearsolutiontask.controller;


import jakarta.validation.Valid;
import com.example.clearsolutiontask.dto.UserRequestDTO;
import com.example.clearsolutiontask.dto.UserResponseDTO;
import com.example.clearsolutiontask.exception.DataProcessingException;
import com.example.clearsolutiontask.mapper.UserMapper;
import com.example.clearsolutiontask.model.User;
import com.example.clearsolutiontask.service.UserService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@RequestParam Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(userMapper.userToUserResponseDTO(user));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO userDTO) {
        User user = userService.create(userMapper.userRequestDTOToUser(userDTO));
        return ResponseEntity.ok(userMapper.userToUserResponseDTO(user));
    }

    @PutMapping(value = "/update",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> update(@RequestParam Long id, @RequestBody @Valid UserRequestDTO userDTO) {
        User updatedUser = userService.update(id, userMapper.userRequestDTOToUser(userDTO));
        return ResponseEntity.ok(userMapper.userToUserResponseDTO(updatedUser));
    }

    @DeleteMapping(value = "/delete")
    public void delete(@RequestParam Long id) {
        userService.deleteById(id);
    }

    @GetMapping(value = "/search-by-date")
    public ResponseEntity<List<UserResponseDTO>> findUsersByBirthDateBetween(@RequestParam(name = "dateFrom") LocalDate from,
                                             @RequestParam(name = "dateTo") LocalDate to) {
        return ResponseEntity.ok(userService.findUsersByBirthDateBetween(from, to)
                .stream()
                .map(userMapper::userToUserResponseDTO)
                .toList());
    }
}

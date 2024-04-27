package com.example.clearsolutiontask.service;

import com.example.clearsolutiontask.exception.DataProcessingException;
import com.example.clearsolutiontask.exception.DateProcessingException;
import com.example.clearsolutiontask.exception.DuplicateKeyException;
import com.example.clearsolutiontask.model.User;
import com.example.clearsolutiontask.repository.UserRepository;
import com.example.clearsolutiontask.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("email.email1@gmail.com",
                "firstName", "lastName",
                LocalDate.of(2000, 1, 1),
                "address 1", "+380062990631");
        testUser.setId(1L);
    }

    @Test
    void findById() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testUser));

        // when
        User actualUser = userService.findById(testUser.getId());

        // then
        assertThat(actualUser).isNotNull();
    }

    @Test
    void findById_throwsDataProcessingException() {
        // given
        Long nonExistingId = 99L;
        when(userRepository.findById(nonExistingId))
                .thenThrow(new DataProcessingException(String.format("User with id: %s not found", nonExistingId)));

        // when
        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> userService.findById(nonExistingId));

        // then
        Assertions.assertEquals("User with id: 99 not found", exception.getMessage());
    }

    @Test
    void create() {
        // given
        when(userRepository.save(testUser)).thenReturn(testUser);

        // when
        User actualUser = userService.create(testUser);

        // then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getId()).isEqualTo(testUser.getId());
        assertThat(actualUser.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void create_existingEmailThrowsDuplicateKeyException() {
        // given
        when(userRepository.save(testUser))
                .thenThrow(new DataIntegrityViolationException("Key (email)=(email.email1@gmail.com) already exists."));

        // when
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class,
                () -> userService.create(testUser));

        // then
        Assertions.assertEquals(String.format("User with \"%s\" field already exists", testUser.getEmail()), exception.getMessage());
    }

    @Test
    void create_existingPhoneNumberThrowsDuplicateKeyException() {
        // given
        when(userRepository.save(testUser))
                .thenThrow(new DataIntegrityViolationException("Key (phoneNumber)=(+380062990631) already exists."));

        // when
        DuplicateKeyException exception = assertThrows(DuplicateKeyException.class,
                () -> userService.create(testUser));

        // then
        Assertions.assertEquals(String.format("User with \"%s\" field already exists", testUser.getPhoneNumber()), exception.getMessage());
    }

    @Test
    void update() {
        // given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // when
        User actualUser = userService.update(testUser.getId(), testUser);

        // then
        assertThat(actualUser).isNotNull();
    }

    @Test
    void deleteById() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testUser));

        // then
        assertAll(() -> userService.deleteById(testUser.getId()));
    }

    @Test
    void findUsersByBirthDateBetween() {
        // when
        List<User> users = List.of(testUser);
        when(userRepository.findUsersByBirthDateBetween(testUser.getBirthDate().minusYears(1),
               testUser.getBirthDate().plusYears(1))).thenReturn(users);

        // when
        List<User> actualUsers = userService.findUsersByBirthDateBetween(testUser.getBirthDate().minusYears(1),
                testUser.getBirthDate().plusYears(1));

        // then
        assertThat(actualUsers.size()).isEqualTo(users.size());
        }

    @Test
    void findUsersByBirthDateBetween_dateFromExceedDateTo() {
        // when
        DateProcessingException exception = assertThrows(DateProcessingException.class,
                () -> userService.findUsersByBirthDateBetween(testUser.getBirthDate().plusYears(1),
                        testUser.getBirthDate().minusYears(1)));

        // then
        Assertions.assertEquals("Date \"from\" can't exceed the date \"to\"", exception.getMessage());
    }
}
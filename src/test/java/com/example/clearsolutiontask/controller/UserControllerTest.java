package com.example.clearsolutiontask.controller;

import com.example.clearsolutiontask.exception.DataProcessingException;
import com.example.clearsolutiontask.exception.DuplicateKeyException;
import com.example.clearsolutiontask.lib.RegistrationAgeValidator;
import com.example.clearsolutiontask.model.User;
import com.example.clearsolutiontask.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void register_validUserCreation() throws Exception {
        // given
        User validUser = getValidUser();
        given(userService.create(any(User.class))).willReturn(validUser);

        // when
        ResultActions response = mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getValidUser())));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.birthDate", CoreMatchers.is(validUser.getBirthDate().toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(validUser.getEmail())));
    }

    @Test
    void register_userWithExistingUniqueFieldCreation() throws Exception {
        // given
        User validUser = getValidUser();
        given(userService.create(any(User.class)))
                .willThrow(new DuplicateKeyException(String.format("User with \"%s\" field already exists", validUser.getEmail())));

        // when
        ResultActions response = mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getValidUser())));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(String.format("User with \"%s\" field already exists", validUser.getEmail()))));
    }

    @Test
    void register_preFullAgeUserCreation() throws Exception {
        // given
        User preFullAgeUser = getPreFullAgeUser();
        given(userService.create(preFullAgeUser)).willReturn(preFullAgeUser);

        // when
        ResultActions response = mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getPreFullAgeUser())));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.message", CoreMatchers.is("User must be at least 18 years old")));
    }

    @Test
    void register_futureBirthDateUserCreation() throws Exception {
        // given
        User futureBirthDateUser = getFutureBirthDateUser();
        given(userService.create(futureBirthDateUser)).willReturn(futureBirthDateUser);

        // when
        ResultActions response = mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(futureBirthDateUser)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(400)))
                .andExpect(jsonPath("$.message", CoreMatchers.is("Birth date must be earlier than current date")));
    }

    @Test
    void findById() throws Exception {
        // given
        User validUser = getValidUser();
        given(userService.findById(validUser.getId())).willReturn(validUser);

        // when
        ResultActions response = mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", validUser.getId().toString()));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(validUser.getId().intValue())))
                .andExpect(jsonPath("$.birthDate", CoreMatchers.is(validUser.getBirthDate().toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(validUser.getEmail())));
    }

    @Test
    void findById_nonExistingIdUser() throws Exception {
        // given
        Long nonExistingUserId = 99L;
        given(userService.findById(nonExistingUserId))
                .willThrow(new DataProcessingException(String.format("User with id: %s not found", nonExistingUserId)));

        // when
        ResultActions response = mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", nonExistingUserId.toString()));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", CoreMatchers.is(404)))
                .andExpect(jsonPath("$.message", CoreMatchers.is(String.format("User with id: %s not found", nonExistingUserId))));
    }

    @Test
    void update_validUserUpdate() throws Exception {
        // given
        User validUser = getValidUser();
        given(userService.update(anyLong(), any(User.class))).willReturn(validUser);

        // when
        ResultActions response = mvc.perform(put("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", validUser.getId().toString())
                .content(mapper.writeValueAsString(getValidUser())));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(validUser.getId().intValue())))
                .andExpect(jsonPath("$.birthDate", CoreMatchers.is(validUser.getBirthDate().toString())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(validUser.getEmail())));
    }

    @Test
    void delete_existingUserDeletion() throws Exception {
        // given
        User validUser = getValidUser();
        given(userService.findById(validUser.getId())).willReturn(validUser);
        doNothing().when(userService).deleteById(anyLong());

        // when
        ResultActions response = mvc.perform(delete("/users/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", validUser.getId().toString()));

        // then
        response.andExpect(status().isNoContent());
    }


    @Test
    void findUsersByBirthDateBetween() throws Exception {
        // given
        User validUser = getValidUser();
        List<User> validUsers = List.of(validUser);
        given(userService.findUsersByBirthDateBetween(any(LocalDate.class), any(LocalDate.class))).willReturn(validUsers);
        // when
        ResultActions response = mvc.perform(get("/users/search-by-date")
                .contentType(MediaType.APPLICATION_JSON)
                .param("dateFrom", String.valueOf(validUser.getBirthDate().minusYears(1)))
                .param("dateTo", String.valueOf(validUser.getBirthDate().plusYears(1))));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(validUsers.size())))
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(validUser.getId().intValue())));
    }

    public User getValidUser() {
        User user = new User("email.email1@gmail.com", "firstName",
                "lastName", LocalDate.of(2000, 1, 1),
                "address 1", "+380062990631");
        user.setId(1L);
        return user;
    }

    public User getPreFullAgeUser() {
        User user = new User("email.email2@gmail.com", "firstName",
                "lastName", LocalDate.now().minusYears(17),
                "address 1", "+380062990632");
        user.setId(2L);
        return user;
    }

    public User getFutureBirthDateUser() {
        User user = new User("email.email2@gmail.com", "firstName",
                "lastName", LocalDate.now().plusYears(1),
                "address 1", "+380062990633");
        user.setId(3L);
        return user;
    }
}
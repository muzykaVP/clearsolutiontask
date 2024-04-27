package com.example.clearsolutiontask.repository;

import com.example.clearsolutiontask.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("email.email1@gmail.com",
                "firstName", "lastName",
                LocalDate.of(2000, 1, 1),
                "address 1", "+380062990631");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
    }

    @Test
    void findById() {
        // when
        Optional<User> actualUser = userRepository.findById(testUser.getId());

        // then
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void create() {
        // when
        User actualUser = userRepository.save(testUser);

        // then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getId()).isEqualTo(testUser.getId());
    }

    @Test
    void update() {
        // when
        testUser.setAddress("another address");
        testUser.setEmail("another.email@gmail.com");
        User actualUser = userRepository.save(testUser);

        // then
        assertThat(actualUser.getId()).isEqualTo(testUser.getId());
        assertThat(actualUser.getAddress()).isNotEqualTo("address 1");
        assertThat(actualUser.getEmail()).isNotEqualTo("email.email1@gmail.com");
    }

    @Test
    void deleteById() {
        // when
        userRepository.deleteById(testUser.getId());
        Optional<User> actualUser = userRepository.findById(testUser.getId());

        // then
        assertThat(actualUser).isEmpty();
    }

    @Test
    void findUsersByBirthDateBetween() {
        // when
        User user = userRepository.save(testUser);
        List<User> expectedUsers = userRepository.findUsersByBirthDateBetween(testUser.getBirthDate().minusYears(1),
                testUser.getBirthDate().plusYears(1));

        // then
        assertThat(expectedUsers.size()).isGreaterThan(0);
        assertThat(expectedUsers.get(0).getId()).isEqualTo(user.getId());
    }
}
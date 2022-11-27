package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class UserDBStorageTest {
    private final UserDBStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeAll

    static public void setUp() {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) VALUES ('mail@mail.ru','dolor',1946-08-20,'Nick Name')";
        //"DELETE FROM film_genre WHERE film_id = ?;";
        //jdbcTemplate.update(sqlQuery);;
    }

    @Test
    public void testFindUser() {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')";
        jdbcTemplate.update(sqlQuery);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

    }
    @Test
    void findAlltest() {
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.findAll());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(0)
                );
    }
    @Test
    void findAlltest2() {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')";
        jdbcTemplate.update(sqlQuery);
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.findAll());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(2)
                );
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }


    @Test
    void addFriend() {
    }

    @Test
    void deleteFriend() {
    }

    @Test
    void getFriends() {
    }
}
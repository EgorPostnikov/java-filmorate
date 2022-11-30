package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDBStorageTest {
    private final UserDBStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM friends");
        jdbcTemplate.update("DELETE FROM USERS ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
    }

    @Test
    void createTest() {
        User user1 = new User(9, "mail@mail.ru", "dolor", "Created user", LocalDate.now());
        Optional<User> userOptional = Optional.ofNullable(userStorage.create(user1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Created user")
                );
    }

    @Test
    public void findUserTest() {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) " +
                "VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')";
        jdbcTemplate.update(sqlQuery);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void findAllTestEmpty() {
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.findAll());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(0)
                );
    }

    @Test
    void findAllTest2() {
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME)" +
                " VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')");
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) " +
                "VALUES ('mail@mail2.ru','dolor2','1947-08-20','Nick Name2')");
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.findAll());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(2)
                );
    }

    @Test
    void update() {
        String sqlQuery = "INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')";
        jdbcTemplate.update(sqlQuery);
        User user1 = new User(1, "mail@mail.ru", "dolor", "Updated user", LocalDate.now());
        Optional<User> userOptional = Optional.ofNullable(userStorage.update(user1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Updated user")
                );
    }

    @Test
    void addFriendTest() throws UnsupportedIdException {
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name)" +
                " VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');");
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name)" +
                " VALUES ('mail@mail2.ru','dolor2','1946-08-20','Name2')");
        Optional<User> userOptional = Optional.ofNullable(userStorage.addFriend(1, 2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(1)
                );
    }

    @Test
    void deleteFriendTest() {
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');");
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name)" +
                " VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');");
        Optional<User> userOptional = Optional.ofNullable(userStorage.addFriend(1, 2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(1)
                );
        Optional<User> userOptional2 = Optional.ofNullable(userStorage.deleteFriend(1, 2));
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user.getFriends()).hasSize(0)
                );
    }

    @Test
    void getFriends() throws SQLException {
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');");
        jdbcTemplate.update("INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail2.ru','dolor2','1946-08-20','Name2');");
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (1,2);");
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.getFriends(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(friends ->
                        assertThat(friends).hasSize(1)
                );
    }

    @Test
    void getCommonFriendTest() {
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME)" +
                " VALUES ('mail@mail.ru','dolor','1946-08-20','Nick Name')");
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) " +
                "VALUES ('mail@mail2.ru','dolor2','1947-08-20','Nick Name2')");
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) " +
                "VALUES ('mail@mail3.ru','dolor3','1947-08-20','Nick Name3')");
        jdbcTemplate.update("INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) " +
                "VALUES ('mail@mail4.ru','dolor4','1947-08-20','Nick Name4')");
        Optional<Collection<User>> userOptional = Optional.ofNullable(userStorage.getCommonFriends(1, 2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(0)
                );
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (1,4)");
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (2,4)");
        Optional<Collection<User>> userOptional2 = Optional.ofNullable(userStorage.getCommonFriends(1, 2));
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasSize(1)
                );
    }

}
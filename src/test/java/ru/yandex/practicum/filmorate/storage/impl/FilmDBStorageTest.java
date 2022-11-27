package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FilmDBStorageTest {

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void findFilm() {
    }

    @Test
    void create() {
    }

    @SpringBootTest
    @AutoConfigureTestDatabase
    @RequiredArgsConstructor(onConstructor_ = @Autowired)
    static
    class UserDbStorageTests {

        
        private final UserStorage userStorage;

        @Test
        public void testFindUserById() {

            Optional<User> userOptional = Optional.ofNullable(userStorage.findUser(1));

            assertThat(userOptional)
                    .isPresent()
                    .hasValueSatisfying(user ->
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                    );
        }


        @Test
        void findAll() {
        }

        @Test
        void create() {
        }

        @Test
        void update() {
        }

        @Test
        void findUser() {
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
}
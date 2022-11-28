package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDBStorageTest {
    private final FilmDBStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM film_genre");
        jdbcTemplate.update("DELETE FROM films ");
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1");
    }

    @Test
    void createTest() {
        Film film1 = new Film(1, "Created film", "Описание",
                LocalDate.of(1999, 8, 20), 120);
        Genre genre = new Genre(1, "");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        film1.setGenres(genres);
        film1.setMpa(new Mpa(1, ""));
        Optional<Film> userOptional = Optional.ofNullable(filmStorage.create(film1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Created film")
                );
    }

    @Test
    void findFilmTest() {
        String sqlQuery = "INSERT INTO films (name,description, releasedate, duration, rating_id)" +
                " VALUES ('Finded film','Описание','1999-08-20','20','1');";
        jdbcTemplate.update(sqlQuery);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilm(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void updateTest() {
        String sqlQuery = "INSERT INTO films (name,description, releasedate, duration, rating_id)" +
                " VALUES ('UnUpdatedFilm','Описание','1999-08-20','20','1');";
        jdbcTemplate.update(sqlQuery);
        Film film1 = new Film(1, "Updated film", "Описание",
                LocalDate.of(1999, 8, 20), 120);
        Genre genre = new Genre(1, "");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        film1.setGenres(genres);
        film1.setMpa(new Mpa(1, ""));
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.update(film1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Updated film")
                );
    }

    @Test
    void findAllTestEmptyTest() {
        Optional<Collection<Film>> filmOptional = Optional.ofNullable(filmStorage.findAll());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasSize(0)
                );
    }

    @Test
    void findAllTest1() {
        String sqlQuery = "INSERT INTO films (name,description, releasedate, duration, rating_id)" +
                " VALUES ('FindAll film','Описание','1999-08-20','20','1');";
        jdbcTemplate.update(sqlQuery);
        Optional<Collection<Film>> filmOptional = Optional.ofNullable(filmStorage.findAll());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(films ->
                        assertThat(films).hasSize(1)
                );
    }

    @Test
    void getGenres() {
        Optional<Collection<Genre>> genresOptional = Optional.ofNullable(filmStorage.getGenres());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        assertThat(genres).hasSize(6)
                );
    }

    @Test
    void getGenreTest() {
        Optional<Genre> genreOptional = Optional.ofNullable(filmStorage.getGenre(6));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Боевик")
                );
    }

    @Test
    void getRatingsTest() {
        Optional<Collection<Mpa>> ratingsOptional = Optional.ofNullable(filmStorage.getRatings());
        assertThat(ratingsOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasSize(5)
                );
    }

    @Test
    void getRatingTest() {
        Optional<Mpa> ratingOptional = Optional.ofNullable(filmStorage.getRating(5));
        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("name", "NC-17")
                );
    }

    @Test
    void addLikeTest() {
        String fillDB = "INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');" +
                "INSERT INTO films (name,description, releasedate, duration, rating_id) " +
                "VALUES ('Added like film','Описание','1999-08-20','20','1');";
        jdbcTemplate.update(fillDB);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addLike(1, 1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().get(0)).isEqualTo(1)
                );
    }

    @Test
    void deleteLikeTest() {
        String fillDB = "INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');" +
                "INSERT INTO films (name,description, releasedate, duration, rating_id) " +
                "VALUES ('DeletedLike Film','Описание','1999-08-20','20','1');";
        jdbcTemplate.update(fillDB);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addLike(1, 1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes()).hasSize(1)
                );
        Optional<Film> filmOptional2 = Optional.ofNullable(filmStorage.deleteLike(1, 1));
        assertThat(filmOptional2)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes()).hasSize(0)
                );
    }

    @Test
    void getMostLikedFilmsTest() {
        String fillDB = "INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail1.ru','dolor1','1946-08-20','Name1');" +
                "INSERT INTO users (email, login, birthday, name) " +
                "VALUES ('mail@mail2.ru','dolor2','1946-08-20','Name2');" +
                "INSERT INTO films (name,description, releasedate, duration, rating_id)" +
                " VALUES ('Матрица','Описание','1999-08-20','20','1');" +
                "INSERT INTO films (name,description, releasedate, duration, rating_id)" +
                " VALUES ('Матрица Перезагрузка','Описание','2000-08-20','20','1');" +
                "INSERT INTO likes (film_id,user_id) VALUES (2,1);";
        jdbcTemplate.update(fillDB);

        List<Film> films = (List<Film>) filmStorage.getMostLikedFilms(10);
        assertEquals("Матрица Перезагрузка", films.get(0).getName());

        String fillDB2 = "INSERT INTO likes (film_id,user_id) VALUES (1,2);" +
                "INSERT INTO likes (film_id,user_id) VALUES (1,1);";
        jdbcTemplate.update(fillDB2);
        List<Film> films2 = (List<Film>) filmStorage.getMostLikedFilms(10);
        assertEquals("Матрица", films2.get(0).getName());
    }
}
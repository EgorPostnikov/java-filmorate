package ru.yandex.practicum.filmorate.storage.impl.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmStorageUtils {
    private static JdbcTemplate jdbcTemplate;

    public FilmStorageUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getInt("film_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("releaseDate").toLocalDate(),
                resultSet.getInt("duration"));
        film.setMpa(new Mpa(resultSet.getInt("rating_id"), resultSet.getString("ratings.name")));
        film.setGenres(getGenreOfFilm(film));
        film.setLikes(getLikesOfFilm(film));
        return film;
    }

    public static List<Genre> getGenreOfFilm(Film film) {
        String sql = "SELECT * FROM film_genre AS fg LEFT OUTER JOIN genres AS ge ON fg.genre_id=ge.genre_id WHERE film_id= ?";
        return jdbcTemplate.query(sql, GenreStorageUtils::makeGenre, film.getId());
    }

    public static List<Long> getLikesOfFilm(Film film) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ? ORDER BY user_id DESC;";
        return jdbcTemplate.query(sql, FilmStorageUtils::makeFriends, film.getId());
    }
    public static Long makeFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

}
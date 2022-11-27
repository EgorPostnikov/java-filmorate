package ru.yandex.practicum.filmorate.storage.impl.utils;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreStorageUtils {

    private GenreStorageUtils() {
    }

    public static Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre(
                resultSet.getInt("genre_id"),
                resultSet.getString("name"));
        return genre;
    }
}
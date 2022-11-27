package ru.yandex.practicum.filmorate.storage.impl.utils;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaStorageUtils {

    private MpaStorageUtils() {
    }

    public static Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(
                resultSet.getInt("rating_id"),
                resultSet.getString("name"));
        return mpa;
    }
}
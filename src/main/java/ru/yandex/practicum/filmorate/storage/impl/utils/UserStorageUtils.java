package ru.yandex.practicum.filmorate.storage.impl.utils;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStorageUtils {

    public static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(
                resultSet.getInt("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
        return user;
    }
}


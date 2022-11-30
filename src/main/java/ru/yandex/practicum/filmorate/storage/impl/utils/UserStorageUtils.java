package ru.yandex.practicum.filmorate.storage.impl.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public class UserStorageUtils {
    private static JdbcTemplate jdbcTemplate;
    public UserStorageUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public static User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(
                resultSet.getInt("users.user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
        user.setFriends(getFriends(user));
        return user;
    }
    public static List<Long> getFriends(User user) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ? ORDER BY friend_id;";
        return jdbcTemplate.query(sql, UserStorageUtils::makeFriends, user.getId());
    }
    public static Long makeFriends(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("friend_id");
    }
}


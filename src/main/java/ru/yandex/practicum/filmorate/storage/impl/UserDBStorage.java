package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.utils.FriendStorageUtils;
import ru.yandex.practicum.filmorate.storage.impl.utils.UserStorageUtils;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;


@Component
@Qualifier("UserDBStorage")
public class UserDBStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserDBStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select * from USERS";
        List<User> users = new ArrayList<>();
        users = jdbcTemplate.query(sql, UserStorageUtils::makeUser);
        log.info("List of all users sent, films qty - {}", users.size());
        return users;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, BIRTHDAY,NAME) VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setDate(3, Date.valueOf(user.getBirthday()));
            stmt.setString(4, user.getName());
            return stmt;
        }, keyHolder);
        int userId = (int) keyHolder.getKey().longValue();
        log.info("User with id {} saved", userId);
        return findUser(userId);
    }

    @Override
    public User update(User user) throws NoSuchElementException {
        findUser(user.getId());
        String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, BIRTHDAY = ?, NAME = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(), user.getId());
        log.info("User with id {} updated", user.getId());
        return findUser(user.getId());
    }

    @Override
    public User findUser(int id) throws NoSuchElementException {
        String sql = "SELECT * from users where user_id = ?";
        List<User> user = jdbcTemplate.query(sql, UserStorageUtils::makeUser, id);
        if (user.size() != 1) {
            throw new NoSuchElementException("User with id " + id + " didn't found!");
        }
        return user.get(0);
    }

    @Override
    public User addFriend(int id, int friendId) {
        findUser(id);
        findUser(friendId);
        String sqlQuery = "INSERT INTO FRIENDS (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.info("User with id {} updated friends", id);
        return findUser(id);
    }

    @Override
    public User deleteFriend(int id, int friendId) throws NoSuchElementException {
        findUser(id);
        findUser(friendId);
        String sqlQuery = "DELETE FROM FRIENDS WHERE user_id= ? and friend_id= ?; ";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.info("User with id {} deleted friends", id);
        return findUser(id);
    }

    @Override
    public Collection<Long> getFriends(int id) throws SQLException {
        String sql = "SELECT FRIEND_ID from FRIENDS where USER_ID = ? order by FRIEND_ID desc;";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        Collection<Long> friends = new ArrayList<>();
        while (userRows.next()) {
            friends.add(userRows.getLong("FRIEND_ID"));
        }
        log.info("List of all friends sent, films qty - {}", friends.size());
        return friends;
    }
}

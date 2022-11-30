package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.utils.UserStorageUtils;
import java.sql.Date;
import java.sql.PreparedStatement;
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
        String sql = "SELECT * FROM users";
        List<User> users =  jdbcTemplate.query(sql, UserStorageUtils::makeUser);
        log.info("List of all users sent, films qty - {}", users.size());
        return users;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (email, login, birthday, name) VALUES (?,?,?,?)";

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
        String sqlQuery = "UPDATE users SET email = ?, login = ?, birthday = ?, name = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getBirthday(), user.getName(), user.getId());
        log.info("User with id {} updated", user.getId());
        return findUser(user.getId());
    }

    @Override
    public User findUser(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        List<User> user = jdbcTemplate.query(sql, UserStorageUtils::makeUser, id);
        if (user.size() != 1) {
            throw new NoSuchElementException("User with id " + id + " didn't found!");
        }
        log.info("User with id {} found", id);
        return user.get(0);
    }

    @Override
    public User addFriend(int id, int friendId) {
        findUser(id);
        findUser(friendId);
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.info("User id {} added friend id {}", id, friendId);
        return findUser(id);
    }

    @Override
    public User deleteFriend(int id, int friendId) throws NoSuchElementException {
        findUser(id);
        findUser(friendId);
        String sqlQuery = "DELETE FROM friends WHERE user_id= ? AND friend_id= ?; ";
        jdbcTemplate.update(sqlQuery, id, friendId);
        log.info("User with id {} deleted friends", id);
        return findUser(id);
    }

    @Override
    public Collection<User> getFriends(int id) {
        String sql = "SELECT * FROM users AS u where u.user_id IN " +
                "(SELECT f.friend_id FROM friends AS f WHERE (f.user_id = ?) ORDER BY f.friend_id);";
        List<User> users =  jdbcTemplate.query(sql, UserStorageUtils::makeUser,id);
        log.info("List of friends sent, films qty - {}", users.size());
        return users;
    }

    @Override
    public Collection<User> getCommonFriends (int id, int otherId){
        String sql = "SELECT * FROM users AS u WHERE u.user_id IN (SELECT friend_id FROM friends AS f WHERE f.user_id = ? or f.user_id = ? AND NOT (f.friend_id = ? or f.friend_id = ?) GROUP BY friend_id HAVING Count(*)>1);";
        List<User> users =  jdbcTemplate.query(sql, UserStorageUtils::makeUser,id,otherId,id,otherId);
        log.info("List of common friends sent, films qty - {}", users.size());
        return users;
    }

}


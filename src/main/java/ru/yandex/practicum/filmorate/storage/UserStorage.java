package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User findUser(int id);

    Collection<Long> getFriends(int id) throws SQLException;

    User addFriend(int id, int friendId) throws UnsupportedIdException;

    User deleteFriend(int id, int friendId) throws UnsupportedIdException;

}

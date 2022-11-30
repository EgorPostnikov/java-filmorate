package ru.yandex.practicum.filmorate.service.inter;

import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserServiceInterface {
    Collection<User> findAll();

    User create(User user);

    User update(User user);

    User findUser(int id);

    User addFriend(int id, int friendId) throws UnsupportedIdException;

    User deleteFriend(int id, int friendId) throws UnsupportedIdException;

    Collection<User> getFriends(int id) throws SQLException;

    Collection<User> getCommonFriends(int id, int otherId);
}

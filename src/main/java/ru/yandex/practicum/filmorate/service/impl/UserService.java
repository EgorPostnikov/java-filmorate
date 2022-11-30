package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.inter.UserServiceInterface;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.sql.SQLException;
import java.util.Collection;

@Service
public class UserService implements UserServiceInterface {
    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User findUser(int id) {
        return userStorage.findUser(id);
    }

    @Override
    public User addFriend(int id, int friendId) throws UnsupportedIdException {
        return userStorage.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(int id, int friendId) throws UnsupportedIdException {
        return userStorage.deleteFriend(id, friendId);
    }

    @Override
    public Collection<User> getFriends(int id) throws SQLException {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}

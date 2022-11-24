package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserStorage userStorage;

    public UserService( @Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }


    public User findUser(int id) {
        log.info("User with id {} found", id);
        return userStorage.findUser(id);
    }

    public User addFriend(int id, int friendId) throws UnsupportedIdException {
        log.info("User id {} added friend id {}", id, friendId);
        return userStorage.addFriend(id, friendId);
    }

    public User deleteFriend(int id, int friendId) throws UnsupportedIdException {
        return userStorage.deleteFriend(id,friendId);
    }

    public Collection<User> getFriends(int id) throws SQLException {
        Collection<Long> friendsId = userStorage.getFriends(id);
        log.info("Friends list for User id {} created, friends qty = {}", id, friendsId.size());
        return getFriendsList(friendsId);

    }

    public Collection<User> getCommonFriends(int id, int otherId) throws SQLException {
        Collection<Long> friendsId = userStorage.getFriends(id);
        Collection<Long> friendsIdOfFriend = userStorage.getFriends(otherId);
        friendsId.retainAll(friendsIdOfFriend);
        log.info("Common friends list for Users id {} and id {} created, friends qty = {}", id, otherId, friendsId.size());
        return getFriendsList(friendsId);
    }

    public Collection<User> getFriendsList(Collection<Long> friendsId) {
        Collection<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            friends.add(findUser(Math.toIntExact(friendId)));
        }
        return friends;
    }

}

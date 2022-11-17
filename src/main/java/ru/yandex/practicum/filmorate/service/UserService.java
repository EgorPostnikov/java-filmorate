package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage,UserDao userDao ) {
        this.userStorage = userStorage;
        this.userDao = userDao;
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


    public Optional<User> findUser(int id) {
        log.info("User with id {} found", id);
        return userDao.findUser(id);
    }

    /*public User addFriend(int id, int friendId) {
        User user = userStorage.findUser(id);
        User friend = userStorage.findUser(friendId);
        user.getFriends().add((long) friendId);
        friend.getFriends().add((long) id);
        log.info("User id {} added friend id {}", id, friendId);
        userStorage.update(friend);
        return userStorage.update(user);
    }

    public User deleteFriend(int id, int friendId) throws UnsupportedIdException {
        User user = userStorage.findUser(id);
        User friend = userStorage.findUser(friendId);
        if (!user.getFriends().contains((long) friendId)) {
            throw new UnsupportedIdException("One of ids didn't found!");
        }
        friend.getFriends().remove((long) id);
        user.getFriends().remove((long) friendId);
        log.info("User id {} removed friend id {}", id, friendId);
        userStorage.update(friend);
        return userStorage.update(user);
    }

    public Collection<User> getFriends(int id) {
        Collection<Long> friendsId = findUser(id).getFriends();
        log.info("Friends list for User id {} created, friends qty = {}", id, friendsId.size());
        return getFriendsList(friendsId);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        Collection<Long> friendsId = new ArrayList<>(findUser(id).getFriends());
        Collection<Long> friendsIdOfFriend = findUser(otherId).getFriends();
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
*/

}

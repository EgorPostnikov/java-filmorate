package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) throws ValidationException {
        User newUser = userValidation(user);
        userStorage.create(user);
        return newUser;
    }

    public User update(User user) throws ValidationException {
        User newUser = userValidation(user);
        userStorage.update(newUser);
        return newUser;
    }

    public User userValidation(User user) throws ValidationException {
        if ((user.getEmail().isEmpty()) || !(user.getEmail().contains("@"))) {
            throw new ValidationException("Email is not correct!");
        }
        if ((user.getLogin().isEmpty()) || (user.getLogin().isBlank())) {
            throw new ValidationException("Login is not correct!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday date is not correct!");
        }
        if ((user.getName() == null) || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }
    public User findUser(int id) {
        log.info("User with id {} found", id);
        return userStorage.findUser(id);
    }

    public User addFriend(int id, int friendId ) {
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
        if (!user.getFriends().contains((long)friendId)) {
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
        log.info("Friends list for User id {} created, friends qty = {}", id,friendsId.size());
        return getFriendsList(friendsId);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        Collection<Long> friendsId = new ArrayList<>(findUser(id).getFriends());
        Collection<Long> friendsIdOfFriend = findUser(otherId).getFriends();
        friendsId.retainAll(friendsIdOfFriend);
        log.info("Common friends list for Users id {} and id {} created, friends qty = {}", id, otherId,friendsId.size());
        return getFriendsList(friendsId);
    }

    public Collection<User> getFriendsList (Collection<Long> friendsId){
        Collection<User> friends = new ArrayList<>();
        for (Long friendId:friendsId) {
            friends.add(findUser(Math.toIntExact(friendId)));
        }
        return friends;
    }


}

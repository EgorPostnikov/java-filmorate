package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inter.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(UserStorage.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    static int id;

    public static int takeId() {
        return ++id;
    }

    @Override
    public Collection<User> findAll() {
        log.info("List of all users sent, films qty - {}", users.size());
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(takeId());
        users.put(user.getId(), user);
        log.info("User with id {} saved", user.getId());
        return user;
    }

    @Override
    public User update(User user) throws NoSuchElementException {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("User with id " + user.getId() + " didn't found!");
        }
        users.put(user.getId(), user);
        log.info("User with id {} updated", user.getId());
        return user;
    }

    @Override
    public User findUser(int id) throws NoSuchElementException {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User with id " + id + " didn't found!");
        }
        return users.get(id);
    }

    @Override
    public User addFriend(int id, int friendId) {
        User user = findUser(id);
        User friend = findUser(friendId);
        user.getFriends().add((long) friendId);
        friend.getFriends().add((long) id);
        log.info("User id {} added friend id {}", id, friendId);
        update(friend);
        return update(user);
    }

    @Override
    public User deleteFriend(int id, int friendId) throws UnsupportedIdException {
        User user = findUser(id);
        User friend = findUser(friendId);
        if (!user.getFriends().contains((long) friendId)) {
            throw new UnsupportedIdException("One of ids didn't found!");
        }
        friend.getFriends().remove((long) id);
        user.getFriends().remove((long) friendId);
        log.info("User id {} removed friend id {}", id, friendId);
        update(friend);
        return update(user);
    }
    @Override
    public Collection<User> getFriends(int id)  {
        Collection<Long> friendsId = getFriendsOfUser(id);
        log.info("Friends list for User id {} created, friends qty = {}", id, friendsId.size());
        return getFriendsList(friendsId);
    }
    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        Collection<Long> friendsId = getFriendsOfUser(id);
        Collection<Long> friendsIdOfFriend = getFriendsOfUser(otherId);
        friendsId.retainAll(friendsIdOfFriend);
        log.info("Common friends list for Users id {} and id {} created, friends qty = {}", id, otherId, friendsId.size());
        return getFriendsList(friendsId);
    }

    public Collection<Long> getFriendsOfUser(int id) {
        Collection<Long> friendsId = findUser(id).getFriends();
        log.info("Friends list for User id {} created, friends qty = {}", id, friendsId.size());
        return friendsId;
    }


    public Collection<User> getFriendsList(Collection<Long> friendsId) {
        Collection<User> friends = new ArrayList<>();
        for (Long friendId : friendsId) {
            friends.add(findUser(Math.toIntExact(friendId)));
        }
        return friends;
    }
}

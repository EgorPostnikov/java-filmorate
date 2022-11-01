package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

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
        log.info("List of all users sent, films qty - {}",users.size());
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
}

package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.Response;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping()
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping()
    public User create(@RequestBody User user) throws ValidationException {
        Gson gson = new Gson();
        User newUser = userValidation(user);
        users.put(newUser.getId(), newUser);
        log.info("User  {} saved!", newUser.getName());
        return newUser;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException {
        Gson gson = new Gson();
        User newUser = userValidation(user);
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            log.info("User's info {} updated!", user.getName());
            return newUser;
        }
        throw new NoSuchElementException("User with id " + user.getId() + " didn't found!");
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response handleException(ValidationException exception) {
        log.warn(exception.getMessage(), exception);
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        log.warn(exception.getMessage(), exception);
        return new Response(exception.getMessage());
    }

}

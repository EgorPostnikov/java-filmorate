package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.Response;
import ru.yandex.practicum.filmorate.exception.UnsupportedIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping()
    public User create(@RequestBody User user) throws ValidationException {
        User newUser = userValidation(user);
        return userService.create(newUser);
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException {
        User newUser = userValidation(user);
        return  userService.update(newUser);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable int id) {
        return userService.findUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) throws UnsupportedIdException {
        if (id==friendId){
        throw new UnsupportedIdException("Ids same");
        }
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) throws UnsupportedIdException {
        if (id==friendId){
            throw new UnsupportedIdException("Ids same");
        }
        return userService.deleteFriend(id,friendId);
    }
    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id)  {
        return userService.getFriends(id);
    }
    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id,@PathVariable int otherId)  {
        return userService.getCommonFriends(id,otherId);
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnsupportedIdException.class)
    public Response handleException(UnsupportedIdException exception) {
        log.warn(exception.getMessage(), exception);
        return new Response(exception.getMessage());
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

}

package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
    private int id = 0;
    private String email;
    private String login;
    private LocalDate birthday;
    private String name;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = takeId();
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private int takeId() {
        return ++id;
    }

}

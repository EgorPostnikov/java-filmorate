package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class User {

    private Set<Long> friends = new HashSet<>();
    private int id;
    private String email;
    private String login;
    private LocalDate birthday;
    private String name;
    public User(){

    };

    /*public User(int id, String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.id = id;
    }*/

}

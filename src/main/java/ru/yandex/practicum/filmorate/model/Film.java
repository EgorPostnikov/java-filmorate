package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;
    private int duration;
    private String description;
    private LocalDate releaseDate;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.id = takeId();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    private int takeId() {
        return ++id;
    }
}


package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Set<Long> likes = new HashSet<>();
    private Set<String> genres = new HashSet<>();
    private int id;

    private int ratingId;
    private String name;
    private int duration;
    private String description;
    private LocalDate releaseDate;

    private String rating;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
    }
}


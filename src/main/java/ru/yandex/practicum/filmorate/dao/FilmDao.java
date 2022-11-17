package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

public interface FilmDao {
    Optional<Film> findFilm(int id);
}

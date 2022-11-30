package ru.yandex.practicum.filmorate.storage.inter;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film findFilm(int id);

    Collection<Genre> getGenres();

    Genre getGenre(int id);

    Collection<Mpa> getRatings();

    Mpa getRating(int id);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    Collection<Film> getMostLikedFilms(int count);
}

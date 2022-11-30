package ru.yandex.practicum.filmorate.service.inter;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.util.Collection;

public interface FilmServiceInterface {

    Collection<Film> findAll();

    Film findFilm(int id);

    Film create(Film film);

    Film update(Film film);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    Collection<Film> getMostLikedFilms(int count);

    Collection<Genre> getGenres();

    Genre getGenre(int id);

    Collection<Mpa> getRatings();

    Mpa getRating(int id);
}

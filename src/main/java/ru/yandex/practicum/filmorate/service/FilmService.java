package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.util.Collection;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilm(int id) {
        return filmStorage.findFilm(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public Film deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getMostLikedFilms(int count) {
        return filmStorage.getMostLikedFilms(count);
    }

    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    public Collection<Mpa> getRatings() {
        return filmStorage.getRatings();
    }

    public Mpa getRating(int id) {
        return filmStorage.getRating(id);
    }
}

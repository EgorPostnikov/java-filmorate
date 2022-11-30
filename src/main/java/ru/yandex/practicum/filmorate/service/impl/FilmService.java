package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.inter.FilmServiceInterface;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.util.Collection;

@Service
public class FilmService implements FilmServiceInterface {

    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Film findFilm(int id) {
        return filmStorage.findFilm(id);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public Collection<Film> getMostLikedFilms(int count) {
        return filmStorage.getMostLikedFilms(count);
    }

    @Override
    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    @Override
    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    @Override
    public Collection<Mpa> getRatings() {
        return filmStorage.getRatings();
    }

    @Override
    public Mpa getRating(int id) {
        return filmStorage.getRating(id);
    }

}

package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilm(int id) {
        log.info("Film with id {} found", id);
        return filmStorage.findFilm(id);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        film.getLikes().add((long) userId);
        log.info("Like with id {} added to film id {}", userId, filmId);
        return filmStorage.update(film);
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        if (!film.getLikes().contains((long) userId)) {
            throw new NoSuchElementException("like of user with id " + userId + " for film id " + filmId + " didn't found!");
        } else {
            film.getLikes().remove((long) userId);
            log.info("Like with id {} removed from film id {}", userId, filmId);
        }
        return filmStorage.update(film);
    }

    public Collection<Film> getMostLikedFilms(int count) {
        return filmStorage.findAll().stream().sorted((p0, p1) -> {
            int comp = p1.getLikes().size() - p0.getLikes().size();
            return comp;
        }).limit(count).collect(Collectors.toList());
    }
}

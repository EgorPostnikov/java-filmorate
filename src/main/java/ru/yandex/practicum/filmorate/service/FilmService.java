package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;

    @Autowired
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

    public Film create(Film film) throws ValidationException {
        if (filmValidation(film)) {
            filmStorage.create(film);
        }
        return film;
    }

    public Film update(Film film) throws ValidationException {
        if (filmValidation(film)) {
            filmStorage.update(film);
            return film;
        }
        throw new NoSuchElementException("Film with id " + film.getId() + " didn't found!");
    }

    public Boolean filmValidation(Film film) throws ValidationException {
        if ((film.getName().isEmpty()) || (film.getName().isBlank())) {
            throw new ValidationException("Name is empty!");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Duration is not positive!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Description length extends 200 chars!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("RealiseDate is before 28 december 1895!");
        }
        return true;
    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        film.getLikes().add((long) userId);
        log.info("Like with id {} added to film id {}", userId, filmId);
        return filmStorage.update(film);
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilm(filmId);
        if (film.getLikes().contains((long) userId)) {
            film.getLikes().remove((long) userId);
            log.info("Like with id {} removed from film id {}", userId, filmId);
        } else {
            throw new NoSuchElementException("like of user with id " + userId + " for film id " + filmId + " didn't found!");
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

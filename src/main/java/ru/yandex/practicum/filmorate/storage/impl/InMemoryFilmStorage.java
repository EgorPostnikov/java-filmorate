package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id;

    public int takeId() {
        return ++id;
    }

    @Override
    public Collection<Film> findAll() {
        log.info("List of all films sent, films qty - {}",films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(takeId());
        log.info("Film with id {} saved", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) throws NoSuchElementException {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Film with id " + film.getId() + " didn't found!");
        }
        films.put(film.getId(), film);
        log.info("Film with id {} updated", film.getId());
        return film;
    }

    @Override
    public Film findFilm(int id) throws NoSuchElementException {
        if (!films.containsKey(id)) {
            throw new NoSuchElementException("Film with id " + id + " didn't found!");
        }
        return films.get(id);
    }

    @Override
    public Collection<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenre(int id) {
        return null;
    }

    @Override
    public Collection<Mpa> getRatings() {
        return null;
    }

    @Override
    public Mpa getRating(int id) {
        return null;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        return null;
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        return null;
    }

    @Override
    public Collection<Film> getMostLikedFilms(int count) {
        return null;
    }

    /*public Film addLike(int filmId, int userId) {
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
    }*/
}

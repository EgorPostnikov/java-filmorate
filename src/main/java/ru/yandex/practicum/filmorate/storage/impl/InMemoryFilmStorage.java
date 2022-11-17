package ru.yandex.practicum.filmorate.storage.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

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
}

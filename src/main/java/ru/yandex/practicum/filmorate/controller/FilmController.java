package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.Response;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping()
    public Collection<Film> findAll() {
        log.info("Films send!");
        return films.values();
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidationException {
        if (filmValidation(film)) {
            films.put(film.getId(), film);
            log.info("Film {} saved!", film.getName());
        }
        return film;
    }

    @PutMapping() Film update(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId()) && filmValidation(film)) {
            films.put(film.getId(), film);
            log.info("Film's {} info updated!", film.getName());
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Response handleException(ValidationException exception) {
        log.warn(exception.getMessage(), exception);
        return new Response(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Response handleException(NoSuchElementException exception) {
        log.warn(exception.getMessage(), exception);
        return new Response(exception.getMessage());
    }

}

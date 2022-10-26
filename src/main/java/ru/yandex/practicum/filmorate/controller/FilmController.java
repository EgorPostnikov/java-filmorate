package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.Response;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable int id) {
        return filmService.findFilm(id);
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws ValidationException {
        if (!filmValidation(film)) {
            throw new ValidationException("Validation false!");
        } return filmService.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws ValidationException {
        if (!filmValidation(film)) {
            throw new ValidationException("Validation false!");
        }
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    Film deleteLike(@PathVariable int id,
                    @PathVariable int userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    Collection<Film> getMostLikedFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostLikedFilms(count);
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

}

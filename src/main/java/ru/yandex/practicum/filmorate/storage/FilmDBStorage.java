package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.utils.GenreStorageUtils;
import ru.yandex.practicum.filmorate.storage.impl.utils.MpaStorageUtils;
import ru.yandex.practicum.filmorate.storage.inter.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@Qualifier("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(FilmDBStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "select * from FILMS AS f LEFT JOIN RATINGS AS r on f.RATING_ID = r.RATING_ID";
        List<Film> films = jdbcTemplate.query(sql, FilmStorageUtils::makeFilm);
        log.info("List of all films sent, films qty - {}", films.size());
        return films;
    }

    @Override
    public Film update(Film film) throws NoSuchElementException {
        findFilm(film.getId());
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATING_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        log.info("Film with id {} updated", film.getId());
        setGenre(film);
        return findFilm(film.getId());
    }


    @Override
    public Film findFilm(int id) throws NoSuchElementException {
        String sql = "select * from FILMS AS f LEFT JOIN RATINGS AS r on f.RATING_ID = r.RATING_ID WHERE FILM_ID=?";
        List<Film> films = jdbcTemplate.query(sql, FilmStorageUtils::makeFilm, id);
        if (films.size() != 1) {
            throw new NoSuchElementException("Film with id " + id + " didn't found!");
        }
        Film film = films.get(0);
        return film;
    }

    @Override
    public Collection<Genre> getGenres() {
        String sql = "select * from GENRES";
        List<Genre> genres = jdbcTemplate.query(sql, GenreStorageUtils::makeGenre);
        log.info("List of all genres sent, genres qty - {}", genres.size());
        return genres;
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sql, GenreStorageUtils::makeGenre, id);
        if (genres.size() != 1) {
            throw new NoSuchElementException("Genre with id " + id + " didn't found!");
        }
        return genres.get(0);
    }

    @Override
    public Collection<Mpa> getRatings() {
        String sql = "select * from RATINGS";
        List<Mpa> ratings = jdbcTemplate.query(sql, MpaStorageUtils::makeMpa);
        log.info("List of all ratings sent, ratings qty - {}", ratings.size());
        return ratings;
    }

    @Override
    public Mpa getRating(int id) throws NoSuchElementException {
        String sql = "SELECT * FROM RATINGS WHERE rating_id = ?";
        List<Mpa> ratings = jdbcTemplate.query(sql, MpaStorageUtils::makeMpa, id);
        if (ratings.size() != 1) {
            throw new NoSuchElementException("Rating with id " + id + " didn't found!");
        }
        return ratings.get(0);
    }

    public void setGenre(Film film) {
        List<Genre> genres = new ArrayList<>(film.getGenres());
        String delGenre = "DELETE FROM film_genre WHERE film_id = ?;";
            jdbcTemplate.update(delGenre, film.getId());
        String sql = "MERGE INTO film_genre (film_id,genre_id) VALUES (?,?);";
        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public Film create(Film film) {

        String sqlQuery = "INSERT INTO FILMS (NAME,DESCRIPTION, RELEASEDATE,DURATION, RATING_ID) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            try {
                stmt.setInt(5, film.getMpa().getId());
            } catch (NullPointerException e) {
                stmt.setNull(5, 1);
            }
            return stmt;
        }, keyHolder);
        int filmId = (int) keyHolder.getKey().longValue();
        log.info("Film with id {} saved", filmId);
        film.setId(filmId);
        setGenre(film);
        return findFilm(filmId);
    }
    @Override
    public Film addLike(int filmId, int userId) {
        String sql = "INSERT INTO likes (film_id,user_id) VALUES (?,?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Like with id {} added to film id {}", userId, filmId);
        return findFilm(filmId);
    }
    @Override
    public Film deleteLike(int filmId, int userId) throws NoSuchElementException {
        String sqlQuerry = "SELECT * FROM likes WHERE film_id = ? and user_id=?";
        List ids = jdbcTemplate.queryForList(sqlQuerry, filmId, userId);
        if (ids.size()==0) {
            throw new NoSuchElementException("like of user with id " + userId + " for film id " + filmId + " didn't found!");
        } else {
            String sql = "DELETE FROM likes WHERE film_id = ? and user_id=?";
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Like with id {} removed from film id {}", userId, filmId);
        }
        return findFilm(filmId);
    }
    @Override
    public Collection<Film> getMostLikedFilms(int count) {
        String sql = "select f.film_id, f.name, f.description,f.releasedate,f. duration, f.rating_id, r.NAME " +
                "from FILMS as f " +
                "left outer join likes as l ON f.FILM_ID = l.FILM_ID " +
                "left outer join ratings as r ON f.rating_id=r.rating_id " +
                "group by f.FILM_ID " +
                "ORDER BY count(USER_ID) desc " +
                "limit ?";
        List<Film> films = jdbcTemplate.query(sql, FilmStorageUtils::makeFilm,count);
        log.info("List of most liked films sent, films qty - {}", films.size());
        return films;
    }
}



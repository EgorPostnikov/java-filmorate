DELETE FROM FRIENDS;
DELETE FROM FILM_GENRE;
DELETE FROM LIKES;
DELETE FROM USERS;
DELETE FROM FILMS;
DELETE FROM GENRES;
DELETE FROM RATINGS;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;
ALTER TABLE GENRES ALTER COLUMN GENRE_ID RESTART WITH 1;
ALTER TABLE RATINGS ALTER COLUMN RATING_ID RESTART WITH 1;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;
INSERT INTO GENRES  (NAME)  VALUES ('Комедия');
INSERT INTO GENRES  (NAME)  VALUES ('Драма');
INSERT INTO GENRES  (NAME)  VALUES ('Мультфильм');
INSERT INTO GENRES  (NAME)  VALUES ('Триллер');
INSERT INTO GENRES  (NAME)  VALUES ('Документальный');
INSERT INTO GENRES  (NAME)  VALUES ('Боевик');
INSERT INTO RATINGS (NAME)  VALUES ('G');
INSERT INTO RATINGS (NAME)  VALUES ('PG');
INSERT INTO RATINGS (NAME)  VALUES ('PG-13');
INSERT INTO RATINGS (NAME)  VALUES ('R');
INSERT INTO RATINGS (NAME)  VALUES ('NC-17');

--select * from FILMS AS f LEFT JOIN RATINGS AS r on f.RATING_ID = r.RATING_ID WHERE FILM_ID=2 ;
--SELECT GENRE_ID FROM RATINGS WHERE rating_id = ?
--INSERT INTO film_genre (film_id,genre_id) VALUES (1,1);
--select * from film_genre as fg left join genres as ge ON fg.genre_id=ge.genre_id where film_id= 2;
--select * from FILMS WHERE film_id in ( SELECT FILM_ID from likes group by FILM_ID ORDER BY count(USER_ID) desc limit 10);
--SELECT FILM_ID from likes group by FILM_ID ORDER BY count(USER_ID) desc limit 10;
/*select f.film_id, f.name, f.description,f.releasedate,f. duration, f.rating_id, r.NAME
from FILMS as f
left outer join likes as l ON f.FILM_ID = l.FILM_ID
left outer join ratings as r ON f.rating_id=r.rating_id
group by f.FILM_ID
ORDER BY count(USER_ID) desc
limit 10;*/

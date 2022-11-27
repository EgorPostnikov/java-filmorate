CREATE TABLE IF NOT EXISTS films (
  film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar,
  description varchar,
  releaseDate date,
  duration int,
  rating_id int default null
) ;

CREATE TABLE IF NOT EXISTS film_genre (
  film_id int ,
  genre_id int,
  primary key (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS ratings (
  rating_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar
);

CREATE TABLE  IF NOT EXISTS likes (
  film_id int,
  user_id int,
  primary key (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS users (
  user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar,
  login varchar,
  birthday date,
  name varchar
);

CREATE TABLE IF NOT EXISTS friends (
  user_id int,
  friend_id int,
 primary key (user_id, friend_id)
);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films (film_id);
ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres (genre_id);
--ALTER TABLE films ADD FOREIGN KEY (rating_id) REFERENCES ratings (rating_id);
ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films (film_id);
ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users (user_id);



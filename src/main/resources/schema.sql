CREATE TABLE IF NOT EXISTS films (
  film_id int PRIMARY KEY,
  rating_id int,
  name varchar,
  duration int,
  description varchar,
  realiseDate timestamp
) ;

CREATE TABLE IF NOT EXISTS film_genre (
  film_id int PRIMARY KEY,
  genre_id int
);

CREATE TABLE IF NOT EXISTS genres (
  genre_id int PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS ratings (
  rating_id int PRIMARY KEY,
  name varchar
);

CREATE TABLE  IF NOT EXISTS likes (
  film_id int PRIMARY KEY,
  user_id int
);

CREATE TABLE IF NOT EXISTS users (
  user_id int PRIMARY KEY,
  email varchar,
  login varchar,
  birthday timestamp,
  first_name varchar,
  last_name varchar
);

CREATE TABLE IF NOT EXISTS friends (
  user_id int PRIMARY KEY,
  friend_id int,
  status_id int
);

CREATE TABLE IF NOT EXISTS status (
  status_id int PRIMARY KEY,
  name varchar
);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres (genre_id);

ALTER TABLE films ADD FOREIGN KEY (rating_id) REFERENCES ratings (rating_id);

ALTER TABLE likes ADD FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE likes ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE status ADD FOREIGN KEY (status_id) REFERENCES friends (status_id);

ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users (user_id);

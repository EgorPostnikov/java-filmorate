/*CREATE TABLE "films" (
  "film_id" int PRIMARY KEY,
  "rating_id" int,
  "name" string,
  "duration" int,
  "description" string,
  "realiseDate" LocalDate
);

CREATE TABLE "film_genre" (
  "film_id" int PRIMARY KEY,
  "genre_id" int
);

CREATE TABLE "genres" (
  "genre_id" int PRIMARY KEY,
  "name" string
);

CREATE TABLE "ratings" (
  "rating_id" int PRIMARY KEY,
  "name" string
);

CREATE TABLE "likes" (
  "film_id" int PRIMARY KEY,
  "user_id" long
);

CREATE TABLE "users" (
  "user_id" int PRIMARY KEY,
  "email" string,
  "login" string,
  "birthday" LocalDate,
  "first_name" string,
  "last_name" string
);

CREATE TABLE "friends" (
  "user_id" int PRIMARY KEY,
  "friend_id" int,
  "status_id" int
);

CREATE TABLE "status" (
  "status_id" int PRIMARY KEY,
  "name" string
);

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("genre_id");

ALTER TABLE "films" ADD FOREIGN KEY ("rating_id") REFERENCES "ratings" ("rating_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "status" ADD FOREIGN KEY ("status_id") REFERENCES "friends" ("status_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("user_id");
*/


CREATE TABLE IF NOT EXISTS User_Filmorate
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email     VARCHAR,
    login     VARCHAR,
    name VARCHAR,
    birthday  DATE
);

CREATE TABLE IF NOT EXISTS Mpa
(
    mpa_id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR
);

CREATE TABLE IF NOT EXISTS Genre
(
    genre_id     INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name   VARCHAR
);

CREATE TABLE IF NOT EXISTS Film
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR,
    description   VARCHAR,
    release_date  DATE,
    duration      INTEGER,
    rate          INTEGER,
    mpa_id        INTEGER,
    FOREIGN KEY (mpa_id) REFERENCES mpa(mpa_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Genre_Film
(
    genre_film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id       INTEGER,
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    genre_id      INTEGER,
    FOREIGN KEY (genre_id) REFERENCES genre(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Like_Film
(
    like_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER,
    FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
    user_id INTEGER
);

CREATE TABLE IF NOT EXISTS Friendship
(
    friendship_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    friend_id     INTEGER,
    user_id       INTEGER,
    FOREIGN KEY (user_id) REFERENCES User_Filmorate(id) ON DELETE CASCADE
);

INSERT INTO Genre (name)
VALUES ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

INSERT INTO Mpa (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');
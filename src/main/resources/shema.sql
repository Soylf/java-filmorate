DROP ALL OBJECTS DELETE FILES;

CREATE TABLE IF NOT EXISTS film (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INT,
    mpa_id INT,
    FOREIGN KEY (mpa_id) REFERENCES Mpa(id)
);

-- Создание таблицы Genre
CREATE TABLE IF NOT EXISTS genre (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы Film_Genre (связь "многие ко многим" между Film и Genre)
CREATE TABLE IF NOT EXISTS film_genre (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES Film(id),
    FOREIGN KEY (genre_id) REFERENCES Genre(id)
);

-- Создание таблицы Mpa
CREATE TABLE IF NOT EXISTS mpa (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы User
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Создание таблицы Like_Film (связь "многие ко многим" между User и Film)
CREATE TABLE IF NOT EXISTS like_film (
    user_id INT,
    film_id INT,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (film_id) REFERENCES Film(id)
);

INSERT INTO genre (name)
VALUES ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик');

INSERT INTO mpa (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');


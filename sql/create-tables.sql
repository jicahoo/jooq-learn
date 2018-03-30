DROP TABLE IF EXISTS author;
CREATE TABLE author (
    id int PRIMARY KEY,
    first_name varchar,
    last_name varchar,
    price int,
    amount int,
    publish_date date
);

DROP TABLE IF EXISTS book;
CREATE TABLE book (
    id int PRIMARY KEY,
    name varchar,
    price int,
    publish_date date,
    author_id int
);
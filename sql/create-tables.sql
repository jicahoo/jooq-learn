DROP TABLE IF EXISTS author;
CREATE TABLE author (
    id int PRIMARY KEY,
    first_name varchar,
    last_name varchar,
    price int,
    amount int,
    publish_date date
);
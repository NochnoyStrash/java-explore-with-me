drop table if exists users cascade;

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar(320) unique,
first_name varchar(100),
last_name varchar(100));

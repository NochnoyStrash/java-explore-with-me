drop table if exists users cascade;
drop table if exists categories cascade;
drop table if exists locations cascade;
drop table if exists events cascade;
drop table if exists requests cascade;
drop table if exists compilations cascade;
drop table if exists compilations_event cascade;

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar(254) unique,
name varchar(250));

CREATE TABLE IF NOT EXISTS categories (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(320) unique);

CREATE TABLE IF NOT EXISTS locations (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
lat FLOAT NOT NULL,
lon FLOAT NOT NULL);

CREATE TABLE IF NOT EXISTS events (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
annotation varchar(2000)  NOT NULL,
category_id BIGINT references categories(id) NOT NULL,
confirmed_Requests BIGINT,
created_On TIMESTAMP WITHOUT TIME ZONE,
description varchar(7000),
event_Date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
initiator_id BIGINT references users(id) NOT NULL,
location_id BIGINT references locations(id) NOT NULL,
paid boolean NOT NULL,
participant_Limit int,
published_On TIMESTAMP WITHOUT TIME ZONE,
request_Moderation boolean,
state varchar(36),
title varchar(500),
views BIGINT);

CREATE TABLE IF NOT EXISTS requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
event_id BIGINT references events(id) NOT NULL,
requester_id BIGINT references users(id) NOT NULL,
status varchar(50));

CREATE TABLE IF NOT EXISTS compilations (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
pinned boolean NOT NULL,
title varchar(1000) NOT NULL);

CREATE TABLE IF NOT EXISTS compilations_event (
comp_id BIGINT references compilations(id),
event_id BIGINT references events(id),
primary key (comp_id, event_id));







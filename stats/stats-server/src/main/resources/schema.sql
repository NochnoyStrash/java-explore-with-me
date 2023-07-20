drop table if exists stats;


CREATE TABLE IF NOT EXISTS stats (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
ip varchar(15) NOT NULL,
uri varchar(1000) NOT NULL,
app varchar(100) NOT NULL,
created TIMESTAMP WITHOUT TIME zone NOT NULL);

--liquibase formatted sql

--changeset DMB:user_table_create
CREATE TABLE IF NOT EXISTS accounts.user (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(60) NOT NULL,
    last_name  VARCHAR(60) NOT NULL,
    gender VARCHAR(1),
    interests  VARCHAR(255),
    city  VARCHAR(60),
    login  VARCHAR(60) NOT NULL ,
    password VARCHAR(60) NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_login_unq UNIQUE (login)
);

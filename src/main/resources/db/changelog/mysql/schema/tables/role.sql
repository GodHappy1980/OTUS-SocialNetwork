--liquibase formatted sql

--changeset DMB:role_table_create
CREATE TABLE IF NOT EXISTS  accounts.role
(
    id   INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (id),
    CONSTRAINT role_name_unq UNIQUE (name)
);
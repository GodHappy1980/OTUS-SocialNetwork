--liquibase formatted sql

--changeset DMB:role_table_create
CREATE TABLE IF NOT EXISTS  accounts.user_role
(
    id   INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT user_role_pk PRIMARY KEY (id),
    CONSTRAINT user_role_unq UNIQUE (user_id, role_id)
);
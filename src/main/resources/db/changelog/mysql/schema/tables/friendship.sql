--liquibase formatted sql

--changeset DMB:friendship_table_create
CREATE TABLE IF NOT EXISTS  accounts.friendship
(
    id   INT NOT NULL AUTO_INCREMENT,
    src_user_id INT NOT NULL,
    dst_user_id INT NOT NULL,
    status TINYINT NOT NULL,
    CONSTRAINT friend_pk PRIMARY KEY (id),
    CONSTRAINT friend_unq UNIQUE (src_user_id, dst_user_id)
);
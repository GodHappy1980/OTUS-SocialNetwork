--liquibase formatted sql

--changeset DMB:role_data
INSERT INTO accounts.role(name) values ('ROLE_ADMIN');
INSERT INTO accounts.role(name) values ('ROLE_USER');
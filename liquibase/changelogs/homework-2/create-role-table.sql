--liquibase formatted sql
--changeset Grigorev Alexei:create_role_table
CREATE TABLE role
(
    id   bigserial PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);
--rollback drop table role

--changeset Grigorev Alexei:insert_role_table
INSERT INTO role(name)
VALUES ('ADMIN'),
       ('USER');
--rollback delete from role where name in ('ADMIN', 'USER');
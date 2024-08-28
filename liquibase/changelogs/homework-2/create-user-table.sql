--liquibase formatted sql
--changeset Grigorev Alexei:create_user_table
CREATE TABLE "user"
(
    id       bigserial PRIMARY KEY,
    email    VARCHAR(30)  NOT NULL,
    password VARCHAR(255) NOT NULL
);
--rollback drop table user

----changeset Grigorev Alexei:insert_user_table
INSERT INTO "user" (email, password)
VALUES ('client1@example.com', 'password1'),
       ('client2@example.com', 'password2'),
       ('client3@example.com', 'password3'),
       ('client4@example.com', 'password4'),
       ('client5@example.com', 'password5'),
       ('client6@example.com', 'password6'),
       ('client7@example.com', 'password7'),
       ('client8@example.com', 'password8'),
       ('client9@example.com', 'password9'),
       ('client10@example.com', 'password10'),
       ('client11@example.com', 'password11'),
       ('client12@example.com', 'password12'),
       ('client13@example.com', 'password13'),
       ('client14@example.com', 'password14'),
       ('client15@example.com', 'password15'),
       ('client16@example.com', 'password16'),
       ('client17@example.com', 'password17'),
       ('client18@example.com', 'password18'),
       ('client19@example.com', 'password19'),
       ('client20@example.com', 'password20'),
       ('client21@example.com', 'password21'),
       ('client22@example.com', 'password22'),
       ('client23@example.com', 'password23'),
       ('client24@example.com', 'password24'),
       ('client25@example.com', 'password25'),
       ('client26@example.com', 'password26'),
       ('client27@example.com', 'password27'),
       ('client28@example.com', 'password28'),
       ('client29@example.com', 'password29'),
       ('client30@example.com', 'password30'),
       ('client31@example.com', 'password31'),
       ('client32@example.com', 'password32'),
       ('client33@example.com', 'password33'),
       ('client34@example.com', 'password34'),
       ('client35@example.com', 'password35'),
       ('client36@example.com', 'password36'),
       ('client37@example.com', 'password37'),
       ('client38@example.com', 'password38'),
       ('client39@example.com', 'password39'),
       ('client40@example.com', 'password40'),
       ('client41@example.com', 'password41'),
       ('client42@example.com', 'password42'),
       ('client43@example.com', 'password43'),
       ('client44@example.com', 'password44'),
       ('client45@example.com', 'password45'),
       ('client46@example.com', 'password46'),
       ('client47@example.com', 'password47'),
       ('client48@example.com', 'password48'),
       ('client49@example.com', 'password49'),
       ('client50@example.com', 'password50');
--rollback truncate "user" ;
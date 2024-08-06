--liquibase formatted sql
--changeset Grigorev Alexei:create_movie_table
CREATE TABLE movie
(
    id           bigserial PRIMARY KEY,
    title        VARCHAR(30)   NOT NULL,
    description  VARCHAR(255)  NOT NULL,
    release_date DATE          NOT NULL,
    rating       NUMERIC(3, 1) NOT NULL
);
--rollback drop table movie

--changeset Grigorev Alexei:insert_movie_table
INSERT INTO movie (title, description, release_date, rating)
VALUES ('title1', 'Description of Movie1', '2024-01-01', 7.5),
       ('Movie2', 'Description of Movie2', '2024-01-02', 8.0),
       ('Movie3', 'Description of Movie3', '2024-01-03', 6.5),
       ('Movie4', 'Description of Movie4', '2024-01-04', 7.0),
       ('Movie5', 'Description of Movie5', '2024-01-05', 8.5),
       ('Movie6', 'Description of Movie6', '2024-01-06', 7.0),
       ('Movie7', 'Description of Movie7', '2024-01-07', 6.0),
       ('Movie8', 'Description of Movie8', '2024-01-08', 7.5),
       ('Movie9', 'Description of Movie9', '2024-01-09', 8.0),
       ('Movie10', 'Description of Movie10', '2024-01-10', 7.5),
       ('Movie11', 'Description of Movie11', '2024-01-11', 6.5),
       ('Movie12', 'Description of Movie12', '2024-01-12', 8.5),
       ('Movie13', 'Description of Movie13', '2024-01-13', 7.0),
       ('Movie14', 'Description of Movie14', '2024-01-14', 6.0),
       ('Movie15', 'Description of Movie15', '2024-01-15', 7.5),
       ('Movie16', 'Description of Movie16', '2024-01-16', 8.0),
       ('Movie17', 'Description of Movie17', '2024-01-17', 7.0),
       ('Movie18', 'Description of Movie18', '2024-01-18', 8.5),
       ('Movie19', 'Description of Movie19', '2024-01-19', 6.5),
       ('Movie20', 'Description of Movie20', '2024-01-20', 7.5),
       ('Movie21', 'Description of Movie21', '2024-01-21', 8.0),
       ('Movie22', 'Description of Movie22', '2024-01-22', 7.0),
       ('Movie23', 'Description of Movie23', '2024-01-23', 6.5),
       ('Movie24', 'Description of Movie24', '2024-01-24', 7.5),
       ('Movie25', 'Description of Movie25', '2024-01-25', 8.0),
       ('Movie26', 'Description of Movie26', '2024-01-26', 7.5),
       ('Movie27', 'Description of Movie27', '2024-01-27', 6.0),
       ('Movie28', 'Description of Movie28', '2024-01-28', 7.5),
       ('Movie29', 'Description of Movie29', '2024-01-29', 8.0),
       ('Movie30', 'Description of Movie30', '2024-01-30', 7.0),
       ('Movie31', 'Description of Movie31', '2024-01-31', 6.5),
       ('Movie32', 'Description of Movie32', '2024-02-01', 7.5),
       ('Movie33', 'Description of Movie33', '2024-02-02', 8.0),
       ('Movie34', 'Description of Movie34', '2024-02-03', 7.0),
       ('Movie35', 'Description of Movie35', '2024-02-04', 8.5),
       ('Movie36', 'Description of Movie36', '2024-02-05', 6.5),
       ('Movie37', 'Description of Movie37', '2024-02-06', 7.0),
       ('Movie38', 'Description of Movie38', '2024-02-07', 8.0),
       ('Movie39', 'Description of Movie39', '2024-02-08', 7.5),
       ('Movie40', 'Description of Movie40', '2024-02-09', 6.0),
       ('Movie41', 'Description of Movie41', '2024-02-10', 7.5),
       ('Movie42', 'Description of Movie42', '2024-02-11', 8.0),
       ('Movie43', 'Description of Movie43', '2024-02-12', 7.0),
       ('Movie44', 'Description of Movie44', '2024-02-13', 6.5),
       ('Movie45', 'Description of Movie45', '2024-02-14', 8.0),
       ('Movie46', 'Description of Movie46', '2024-02-15', 7.5),
       ('Movie47', 'Description of Movie47', '2024-02-16', 8.0),
       ('Movie48', 'Description of Movie48', '2024-02-17', 7.0),
       ('Movie49', 'Description of Movie49', '2024-02-18', 6.5),
       ('Movie50', 'Description of Movie50', '2024-02-19', 8.0);
--rollback delete from user where title in ('Movie1', 'Movie2', 'Movie3', 'Movie4', 'Movie5', 'Movie6', 'Movie7', 'Movie8', 'Movie9', 'Movie10',
-- 'Movie11', 'Movie12', 'Movie13', 'Movie14', 'Movie15', 'Movie16', 'Movie17', 'Movie18', 'Movie19', 'Movie20', 'Movie21', 'Movie22', 'Movie23',
-- 'Movie24', 'Movie25', 'Movie26', 'Movie27', 'Movie28', 'Movie29', 'Movie30', 'Movie31', 'Movie32', 'Movie33', 'Movie34', 'Movie35', 'Movie36',
-- 'Movie37', 'Movie38', 'Movie39', 'Movie40', 'Movie41', 'Movie42', 'Movie43', 'Movie44', 'Movie45', 'Movie46', 'Movie47', 'Movie48', 'Movie49',
-- 'Movie50');
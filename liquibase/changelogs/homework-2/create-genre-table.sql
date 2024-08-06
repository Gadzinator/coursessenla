--liquibase formatted sql
--changeset Grigorev Alexei:create_genre_table
CREATE TABLE genre
(
    id   bigserial PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
--rollback drop table genre

--changeset Grigorev Alexei:insert_genre_table
INSERT INTO genre (name)
VALUES ('Action'),
       ('Adventure'),
       ('Comedy'),
       ('Drama'),
       ('Fantasy'),
       ('Horror'),
       ('Romance'),
       ('Sci-Fi'),
       ('Thriller'),
       ('Western'),
       ('Mystery'),
       ('Crime'),
       ('Documentary'),
       ('Animation'),
       ('Biography'),
       ('History'),
       ('Musical'),
       ('War'),
       ('Family'),
       ('Romantic Comedy'),
       ('Action Comedy'),
       ('Adventure Comedy'),
       ('Science Fiction'),
       ('Fantasy Drama'),
       ('Historical Drama'),
       ('Biographical Drama'),
       ('Political Thriller'),
       ('Superhero'),
       ('Suspense'),
       ('Romantic Drama'),
       ('Teen Drama'),
       ('Legal Drama'),
       ('Medical Drama'),
       ('Action Thriller'),
       ('Psychological Thriller'),
       ('Historical Fiction'),
       ('Supernatural Horror'),
       ('Psychological Horror'),
       ('Slasher'),
       ('Monster'),
       ('Gothic'),
       ('Courtroom Drama'),
       ('Family Comedy'),
       ('Stand-Up Comedy'),
       ('Mockumentary'),
       ('Dark Comedy'),
       ('Animated Comedy'),
       ('Musical Drama'),
       ('Musical Comedy'),
       ('Experimental');
--rollback delete from genre where name in ('Action', 'Adventure', 'Comedy', 'Drama', 'Fantasy', 'Horror', 'Romance', 'Sci-Fi', 'Thriller', 'Western',
-- 'Mystery', 'Crime', 'Documentary', 'Animation', 'Biography', 'History', 'Musical', 'War', 'Family', 'Romantic Comedy', 'Action Comedy',
-- 'Adventure Comedy', 'Science Fiction', 'Fantasy Drama', 'Historical Drama', 'Biographical Drama', 'Political Thriller', 'Superhero', 'Suspense',
-- 'Romantic Drama', 'Teen Drama', 'Legal Drama', 'Medical Drama', 'Action Thriller', 'Psychological Thriller', 'Historical Fiction',
-- 'Supernatural Horror', 'Psychological Horror', 'Slasher', 'Monster', 'Gothic', 'Courtroom Drama', 'Family Comedy', 'Stand-Up Comedy',
-- 'Mockumentary', 'Dark Comedy', 'Animated Comedy', 'Musical Drama', 'Musical Comedy', 'Experimental');
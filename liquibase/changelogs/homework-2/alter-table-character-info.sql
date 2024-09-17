--liquibase formatted sql
--changeset Grigorev Alexei:rename_table_movie_actor_to_character_info
ALTER TABLE movie_actor  RENAME TO character_info;
--rollback alter table character_info

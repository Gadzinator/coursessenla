--liquibase formatted sql
-- changeset Grigorev Alexei:add_index_playlist_user_id
CREATE INDEX idx_playlist_user_id ON playlist (user_id);
--rollback drop index idx_playlist_user_id
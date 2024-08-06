--liquibase formatted sql
-- changeset Grigorev Alexei:add_index_profile_user_id
CREATE INDEX idx_profile_user_id ON profile (user_id);
--rollback drop index idx_profile_user_id
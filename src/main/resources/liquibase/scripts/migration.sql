-- liquibase formatted sql
-- changeset ann:1
CREATE TABLE notification_task (
                       id SERIAL,
                       chat_id bigint,
                       notification_date  date,
                       notification_message text,
                       status varchar,
                       sent_date timestamp
)

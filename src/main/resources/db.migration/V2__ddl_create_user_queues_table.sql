USE helpdesk;
CREATE TABLE IF NOT EXISTS user_queues
(
    user_id bigint,
    queue_id bigint,
    constraint user_id_fk FOREIGN KEY (user_id) REFERENCES system_users(id),
    CONSTRAINT queue_id_fk FOREIGN KEY (queue_id) REFERENCES task_queues(id)
);
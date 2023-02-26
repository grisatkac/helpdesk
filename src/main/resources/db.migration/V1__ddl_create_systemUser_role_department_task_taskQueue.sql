CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name varchar(10) not null
);

CREATE TABLE IF NOT EXISTS system_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
    login VARCHAR(30),
    password VARCHAR(255),
    role_id BIGINT,
    department_id BIGINT,
    CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(200),
    head_id bigint,
    CONSTRAINT head_id_fk FOREIGN KEY (head_id) REFERENCES system_users(id)
);

ALTER TABLE system_users
ADD CONSTRAINT department_id_fk FOREIGN KEY (department_id) REFERENCES departments(id);

CREATE TABLE IF NOT EXISTS task_queues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(200),
    department_id BIGINT,
    CONSTRAINT department_id_for_queue_fk FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(2000),
    created_date date,
    status VARCHAR(30),
    urgency VARCHAR(30),
    owner_id BIGINT,
    executor_id BIGINT,
    queue_id BIGINT,
    CONSTRAINT owner_id FOREIGN KEY (owner_id) REFERENCES  system_users(id),
    CONSTRAINT executor_id FOREIGN KEY (executor_id) REFERENCES system_users(id),
    CONSTRAINT queue_id FOREIGN KEY (queue_id) REFERENCES task_queues(id)
);
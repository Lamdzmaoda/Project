
CREATE TABLE course
(
    id            VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    duration_days date         NULL,
    create_at     time         NULL,
    CONSTRAINT pk_course PRIMARY KEY (id)
);
CREATE TABLE step
(
    id                      VARCHAR(255) NOT NULL,
    title                   VARCHAR(255) NOT NULL,
    type                    VARCHAR(255) NULL,
    mode                    VARCHAR(255) NULL,
    order_index             INT          NOT NULL,
    is_locked               VARCHAR(255) NULL,
    is_completed            VARCHAR(255) NULL,
    xp                      DOUBLE       NOT NULL,
    required_to_unlock_next BIT(1)       NOT NULL,
    data                    LONGTEXT     NULL,
    create_at               datetime     NULL,
    lesson_id               VARCHAR(255) NULL,
    CONSTRAINT pk_step PRIMARY KEY (id)
);

ALTER TABLE step
    ADD CONSTRAINT FK_STEP_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lesson (id);
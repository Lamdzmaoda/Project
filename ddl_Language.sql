CREATE TABLE language
(
    language_name VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    icon          VARCHAR(255) NULL,
    level         INT          NOT NULL,
    duration_days datetime     NULL,
    current_xp    DOUBLE       NOT NULL,
    total_xp      DOUBLE       NOT NULL,
    create_at     datetime     NULL,
    CONSTRAINT pk_language PRIMARY KEY (language_name)
);
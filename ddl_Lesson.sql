CREATE TABLE lesson
(
    id               VARCHAR(255) NOT NULL,
    title            VARCHAR(255) NOT NULL,
    order_index      INT          NOT NULL,
    xp               DOUBLE       NOT NULL,
    progress         DOUBLE       NOT NULL,
    is_locked        VARCHAR(255) NULL,
    is_completed     VARCHAR(255) NULL,
    content_markdown LONGTEXT     NULL,
    chapter_id       VARCHAR(255) NULL,
    create_at        datetime     NULL,
    CONSTRAINT pk_lesson PRIMARY KEY (id)
);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapter (id);
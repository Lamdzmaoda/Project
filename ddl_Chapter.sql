CREATE TABLE chapter
(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    order_index INT          NOT NULL,
    is_locked   VARCHAR(255) NULL,
    language_id VARCHAR(255) NULL,
    create_at   datetime     NULL,
    CONSTRAINT pk_chapter PRIMARY KEY (id)
);

ALTER TABLE chapter
    ADD CONSTRAINT FK_CHAPTER_ON_LANGUAGE FOREIGN KEY (language_id) REFERENCES language (language_name);
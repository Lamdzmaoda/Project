CREATE TABLE chapter
(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    order_index INT          NOT NULL,
    is_locked   VARCHAR(255) NULL,
    status      VARCHAR(255) NOT NULL,
    language_id VARCHAR(255) NULL,
    create_at   datetime NULL,
    CONSTRAINT pk_chapter PRIMARY KEY (id)
);

CREATE TABLE code
(
    id              VARCHAR(255) NOT NULL,
    status          SMALLINT NULL,
    error_type      VARCHAR(255) NULL,
    line            INT NULL,
    column_index    INT NULL,
    error_line_code VARCHAR(255) NULL,
    pointer         VARCHAR(255) NULL,
    message_vn      VARCHAR(255) NULL,
    input           VARCHAR(255) NULL,
    output          VARCHAR(255) NULL,
    CONSTRAINT pk_code PRIMARY KEY (id)
);

CREATE TABLE enrollment
(
    id           VARCHAR(255) NOT NULL,
    current_xp DOUBLE NOT NULL,
    status       VARCHAR(255) NULL,
    progress_percentage DOUBLE NULL,
    enrolled_at  datetime NULL,
    completed_at datetime NULL,
    user_id      VARCHAR(255) NOT NULL,
    language_id  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_enrollment PRIMARY KEY (id)
);

CREATE TABLE invalidated_token
(
    id          VARCHAR(255) NOT NULL,
    expiry_time datetime NULL,
    CONSTRAINT pk_invalidatedtoken PRIMARY KEY (id)
);

CREATE TABLE language
(
    language_name VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    icon          VARCHAR(255) NULL,
    level         INT          NOT NULL,
    duration_days datetime NULL,
    current_xp DOUBLE NOT NULL,
    total_xp DOUBLE NOT NULL,
    status        VARCHAR(255) NOT NULL,
    create_at     datetime NULL,
    CONSTRAINT pk_language PRIMARY KEY (language_name)
);

CREATE TABLE lesson
(
    id               VARCHAR(255) NOT NULL,
    title            VARCHAR(255) NOT NULL,
    order_index      INT          NOT NULL,
    xp DOUBLE NOT NULL,
    progress DOUBLE NOT NULL,
    is_locked        VARCHAR(255) NOT NULL,
    is_completed     VARCHAR(255) NOT NULL,
    status           VARCHAR(255) NOT NULL,
    content_markdown LONGTEXT NULL,
    chapter_id       VARCHAR(255) NULL,
    create_at        datetime NULL,
    CONSTRAINT pk_lesson PRIMARY KEY (id)
);

CREATE TABLE openai
(
    id                 VARCHAR(255) NOT NULL,
    user_message       TEXT NULL,
    ai_explanation     TEXT NULL,
    suggested_code     TEXT NULL,
    motivation_message VARCHAR(255) NULL,
    user_id            VARCHAR(255) NULL,
    step_id            VARCHAR(255) NULL,
    create_at          datetime NULL,
    CONSTRAINT pk_openai PRIMARY KEY (id)
);

CREATE TABLE openai_hints
(
    openai_id VARCHAR(255) NOT NULL,
    hints     VARCHAR(255) NULL
);

CREATE TABLE permission
(
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_permission PRIMARY KEY (name)
);

CREATE TABLE `role`
(
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (name)
);

CREATE TABLE role_permissions
(
    role_name        VARCHAR(255) NOT NULL,
    permissions_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_name, permissions_name)
);

CREATE TABLE step
(
    id                      VARCHAR(255) NOT NULL,
    title                   VARCHAR(255) NOT NULL,
    type                    VARCHAR(255) NOT NULL,
    mode                    VARCHAR(255) NULL,
    order_index             INT          NOT NULL,
    status                  VARCHAR(255) NOT NULL,
    is_locked               VARCHAR(255) NOT NULL,
    is_completed            VARCHAR(255) NOT NULL,
    xp DOUBLE NOT NULL,
    required_to_unlock_next BIT(1)       NOT NULL,
    data                    JSON NULL,
    create_at               datetime NULL,
    lesson_id               VARCHAR(255) NULL,
    CONSTRAINT pk_step PRIMARY KEY (id)
);

CREATE TABLE user
(
    id                 VARCHAR(255) NOT NULL,
    user_name          VARCHAR(255) COLLATE utf8mb4_unicode_ci NULL,
    password           VARCHAR(255) NULL,
    email              VARCHAR(255) NULL,
    first_name         VARCHAR(255) NULL,
    last_name          VARCHAR(255) NULL,
    streak             INT          NOT NULL,
    last_activity_date date NULL,
    birth_date         date NULL,
    total_xp DOUBLE NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_chapter_progress
(
    id           VARCHAR(255) NOT NULL,
    user_id      VARCHAR(255) NOT NULL,
    chapter_id   VARCHAR(255) NOT NULL,
    is_locked    VARCHAR(255) NOT NULL,
    is_completed VARCHAR(255) NOT NULL,
    CONSTRAINT pk_userchapterprogress PRIMARY KEY (id)
);

CREATE TABLE user_lesson_progress
(
    id           VARCHAR(255) NOT NULL,
    user_id      VARCHAR(255) NOT NULL,
    lesson_id    VARCHAR(255) NOT NULL,
    is_locked    VARCHAR(255) NOT NULL,
    is_completed VARCHAR(255) NOT NULL,
    CONSTRAINT pk_userlessonprogress PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id    VARCHAR(255) NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, roles_name)
);

CREATE TABLE user_step_progress
(
    id           VARCHAR(255) NOT NULL,
    user_id      VARCHAR(255) NOT NULL,
    step_id      VARCHAR(255) NOT NULL,
    is_locked    VARCHAR(255) NOT NULL,
    is_completed VARCHAR(255) NOT NULL,
    earned_xp DOUBLE NOT NULL,
    completed_at datetime NULL,
    CONSTRAINT pk_userstepprogress PRIMARY KEY (id)
);

ALTER TABLE enrollment
    ADD CONSTRAINT uc_7f09dd22f045177698cba64db UNIQUE (user_id, language_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (user_name);

ALTER TABLE chapter
    ADD CONSTRAINT FK_CHAPTER_ON_LANGUAGE FOREIGN KEY (language_id) REFERENCES language (language_name);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_LANGUAGE FOREIGN KEY (language_id) REFERENCES language (language_name);

ALTER TABLE enrollment
    ADD CONSTRAINT FK_ENROLLMENT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE lesson
    ADD CONSTRAINT FK_LESSON_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapter (id);

ALTER TABLE openai
    ADD CONSTRAINT FK_OPENAI_ON_STEP FOREIGN KEY (step_id) REFERENCES step (id);

ALTER TABLE openai
    ADD CONSTRAINT FK_OPENAI_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE step
    ADD CONSTRAINT FK_STEP_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lesson (id);

ALTER TABLE user_chapter_progress
    ADD CONSTRAINT FK_USERCHAPTERPROGRESS_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapter (id);

ALTER TABLE user_chapter_progress
    ADD CONSTRAINT FK_USERCHAPTERPROGRESS_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_lesson_progress
    ADD CONSTRAINT FK_USERLESSONPROGRESS_ON_LESSON FOREIGN KEY (lesson_id) REFERENCES lesson (id);

ALTER TABLE user_lesson_progress
    ADD CONSTRAINT FK_USERLESSONPROGRESS_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_step_progress
    ADD CONSTRAINT FK_USERSTEPPROGRESS_ON_STEP FOREIGN KEY (step_id) REFERENCES step (id);

ALTER TABLE user_step_progress
    ADD CONSTRAINT FK_USERSTEPPROGRESS_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE openai_hints
    ADD CONSTRAINT fk_openai_hints_on_open_a_i FOREIGN KEY (openai_id) REFERENCES openai (id);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_name) REFERENCES permission (name);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES `role` (name);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES `role` (name);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES user (id);
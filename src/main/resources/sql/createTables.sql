create table TB_USER
(
    USER_ID             CHAR(36)        NOT NULL,
    AUTH_USER_ID        VARCHAR(100)    NOT NULL,
    DISPLAY_NAME        VARCHAR(300)    NOT NULL,
    CREATE_USER_ID      CHAR(36)        NOT NULL,
    CREATE_TIMESTAMP    TIMESTAMP       NOT NULL,
    UPDATE_USER_ID      CHAR(36)        NOT NULL,
    UPDATE_TIMESTAMP    TIMESTAMP       NOT NULL,
    UNIQUE(AUTH_USER_ID),
    PRIMARY KEY(USER_ID)
);

create table TB_MEMO
(
    MEMO_ID             CHAR(36)        NOT NULL,
    TITLE               VARCHAR(1000)   NOT NULL,
    CONTENT             CLOB            NOT NULL,
    CREATE_USER_ID      CHAR(36)        NOT NULL,
    CREATE_TIMESTAMP    TIMESTAMP       NOT NULL,
    UPDATE_USER_ID      CHAR(36)        NOT NULL,
    UPDATE_TIMESTAMP    TIMESTAMP       NOT NULL,
    PRIMARY KEY(MEMO_ID)
);

CREATE TABLE IF NOT EXISTS MEMBERSHIP
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    gym_id          BIGINT             NOT NULL,
    member_id       BIGINT             NOT NULL,
    membership_type VARCHAR(50)        NOT NULL,
    date_from       TIMESTAMP          NOT NULL,
    date_to         TIMESTAMP          NOT NULL,
    price           DECIMAL(6, 2)      NOT NULL,

    CONSTRAINT membership_uq UNIQUE (gym_id, member_id, date_from)
);
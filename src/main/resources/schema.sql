CREATE TABLE IF NOT EXISTS MEMBERSHIP
(
    gym_id          BIGINT        NOT NULL,
    member_id       BIGINT        NOT NULL,
    membership_type VARCHAR(50)   NOT NULL,
    date_from       TIMESTAMP     NOT NULL,
    date_to         TIMESTAMP     NOT NULL,
    price           DECIMAL(6, 2) NOT NULL

);
--liquibase formatted sql
--changeset cleiton.sena:V20250417_102711-create_tb_user labels 1.0.0

CREATE TABLE JAYA_TECH.tb_user (
    id VARCHAR(36) NOT NULL,
    given_name VARCHAR(50) NOT NULL,
    family_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT pk_tb_user PRIMARY KEY (id)
);

ALTER TABLE JAYA_TECH.tb_user ADD CONSTRAINT uc_tb_user_email UNIQUE (email);

--rollback DROP TABLE JAYA_TECH.tb_user;
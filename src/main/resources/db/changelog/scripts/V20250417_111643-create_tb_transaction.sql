--liquibase formatted sql
--changeset cleiton.sena:V20250417_111643-create_tb_transaction labels 1.0.0

CREATE TABLE JAYA_TECH.tb_transaction (
    id VARCHAR(36) NOT NULL,
    origin_currency CHAR(3) NOT NULL,
    origin_amount DECIMAL(19,2) NOT NULL,
    destination_currency CHAR(3) NOT NULL,
    conversion_rate DECIMAL(11,7) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    CONSTRAINT pk_tb_transaction PRIMARY KEY (id)
);

ALTER TABLE JAYA_TECH.tb_transaction
ADD CONSTRAINT FK_TB_TRANSACTION_ON_USER
FOREIGN KEY (user_id) REFERENCES JAYA_TECH.tb_user (id);

--rollback DROP TABLE JAYA_TECH.tb_transaction;
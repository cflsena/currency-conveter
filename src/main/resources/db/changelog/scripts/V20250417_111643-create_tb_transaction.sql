--liquibase formatted sql
--changeset cleiton.sena:V20250417_111643-create_tb_transaction labels 1.0.0

CREATE TABLE CURRENCY_CONVERSION.tb_transaction (
    id UUID NOT NULL,
    origin_currency CHAR(3) NOT NULL,
    origin_amount DECIMAL(19,2) NOT NULL,
    destination_currency CHAR(3) NOT NULL,
    conversion_rate DECIMAL(11,7) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_tb_transaction PRIMARY KEY (id)
);

ALTER TABLE CURRENCY_CONVERSION.tb_transaction
ADD CONSTRAINT FK_TB_TRANSACTION_ON_USER
FOREIGN KEY (user_id) REFERENCES CURRENCY_CONVERSION.tb_user (id);

CREATE INDEX idx_transactions_user_created_at
  ON CURRENCY_CONVERSION.tb_transaction (user_id, created_at);

--rollback DROP TABLE CURRENCY_CONVERSION.tb_transaction;
--rollback DROP INDEX idx_transactions_user_created_at;
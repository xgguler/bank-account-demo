CREATE TABLE IF NOT EXISTS bank_account (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    account_number varchar NOT NULL ,
    email varchar ,
    created_date datetime NOT NULL,

    PRIMARY KEY (id)
)
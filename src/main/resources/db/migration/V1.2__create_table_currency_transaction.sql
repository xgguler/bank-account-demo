CREATE TABLE IF NOT EXISTS currency_transaction (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    account_id BIGINT NOT NULL ,
    currency varchar(4) NOT NULL ,
    amount decimal(19,2) NOT NULL,
    transaction_type varchar(15) NOT NULL ,
    created_date datetime NOT NULL ,
    foreign key (account_id) references bank_account(id),

    PRIMARY KEY (id)
    )
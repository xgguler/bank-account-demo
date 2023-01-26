CREATE TABLE IF NOT EXISTS currency_balance (
        id BIGINT NOT NULL AUTO_INCREMENT ,
        account_id BIGINT NOT NULL ,
        currency varchar(4) NOT NULL ,
        balance decimal(19,2) NOT NULL ,
        created_date datetime NOT NULL ,
        foreign key (account_id) references bank_account(id) ,
        unique (account_id, currency),

        PRIMARY KEY (id)
    )
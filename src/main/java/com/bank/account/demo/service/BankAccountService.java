package com.bank.account.demo.service;

import com.bank.account.demo.model.Currency;

import java.math.BigDecimal;
import java.util.Map;

public interface BankAccountService {

    void deposit(String accountNumber, BigDecimal amount, Currency currency);
    void withdraw(String accountNumber, BigDecimal amount, Currency currency);
    Map<Currency, BigDecimal> balance(String accountNumber);
    BigDecimal exchange(String accountNumber, BigDecimal amount, Currency from, Currency to);

}

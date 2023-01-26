package com.bank.account.demo.validator;

import com.bank.account.demo.dto.DepositRequest;
import com.bank.account.demo.dto.ExchangeRequest;
import com.bank.account.demo.dto.WithdrawRequest;
import com.bank.account.demo.model.BankAccount;
import com.bank.account.demo.model.Currency;
import com.bank.account.demo.model.CurrencyBalance;
import com.bank.account.demo.model.TransactionType;

import java.math.BigDecimal;


public interface ValidatorService {
    BankAccount validateBankAccount(String accountNumber);
    CurrencyBalance validateCurrencyBalance(BankAccount account, Currency currency, BigDecimal amount, TransactionType type);
    void checkStatus();
    void validateDepositRequest(DepositRequest request);
    void validateWithdrawRequest(WithdrawRequest request);
    void validateBalanceRequest(String accountNumber);
    void validateExchangeRequest(ExchangeRequest request);

}

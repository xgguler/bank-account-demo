package com.bank.account.demo.validator.impl;

import com.bank.account.demo.client.RestClient;
import com.bank.account.demo.dto.DepositRequest;
import com.bank.account.demo.dto.ExchangeRequest;
import com.bank.account.demo.dto.WithdrawRequest;
import com.bank.account.demo.model.BankAccount;
import com.bank.account.demo.model.Currency;
import com.bank.account.demo.model.CurrencyBalance;
import com.bank.account.demo.model.TransactionType;
import com.bank.account.demo.repository.BankAccountRepository;
import com.bank.account.demo.validator.ValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class ValidatorServiceImpl implements ValidatorService {

    private final BankAccountRepository repository;

    private final RestClient restClient;

    public ValidatorServiceImpl(BankAccountRepository repository, RestClient restClient) {
        this.repository = repository;
        this.restClient = restClient;
    }

    @Override
    public BankAccount validateBankAccount(String accountNumber) {
        BankAccount bankAccount = repository.findAccountByAccountNumber(accountNumber);
        if (Objects.isNull(bankAccount)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return bankAccount;
    }

    @Override
    public CurrencyBalance validateCurrencyBalance(BankAccount account, Currency currency, BigDecimal amount, TransactionType type) {
        CurrencyBalance currencyBalance = getBalanceList(account, currency);
        if (type.equals(TransactionType.DEPOSIT)) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return currencyBalance;
        } else {
            if (Objects.isNull(currencyBalance.getBalance())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (currencyBalance.getBalance().compareTo(amount) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

        return currencyBalance;
    }

    @Override
    public void checkStatus() {
        Boolean status = restClient.checkStatus();
        if (!status) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateDepositRequest(DepositRequest request) {
        if (Objects.isNull(request.getAccountNumber()) ||
                Objects.isNull(request.getCurrency()) ||
                Objects.isNull(request.getAmount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateWithdrawRequest(WithdrawRequest request) {
        if (Objects.isNull(request.getAccountNumber()) ||
                Objects.isNull(request.getCurrency()) ||
                Objects.isNull(request.getAmount())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateBalanceRequest(String accountNumber) {
        if (Objects.isNull(accountNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateExchangeRequest(ExchangeRequest request) {
        if (Objects.isNull(request.getAccountNumber()) ||
                Objects.isNull(request.getFrom()) ||
                Objects.isNull(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private CurrencyBalance getBalanceList(BankAccount account, Currency currency) {
        CurrencyBalance currencyBalance = new CurrencyBalance();
        for (CurrencyBalance balance: account.getBalances()) {
            if (balance.getCurrency().equals(currency)) currencyBalance = balance;
        }

        return currencyBalance;
    }
}

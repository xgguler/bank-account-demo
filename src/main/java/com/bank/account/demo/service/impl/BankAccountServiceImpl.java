package com.bank.account.demo.service.impl;

import com.bank.account.demo.model.*;
import com.bank.account.demo.repository.CurrencyBalanceRepository;
import com.bank.account.demo.repository.CurrencyTransactionRepository;
import com.bank.account.demo.service.BankAccountService;
import com.bank.account.demo.service.CurrencyService;
import com.bank.account.demo.validator.ValidatorService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BankAccountServiceImpl implements BankAccountService {

    private final CurrencyBalanceRepository balanceRepository;

    private final CurrencyTransactionRepository transactionRepository;

    private final CurrencyService service;

    private final ValidatorService validatorService;

    public BankAccountServiceImpl(CurrencyBalanceRepository balanceRepository,
                                  CurrencyTransactionRepository transactionRepository,
                                  CurrencyService service,
                                  ValidatorService validatorService) {
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.service = service;
        this.validatorService = validatorService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deposit(String accountNumber, BigDecimal amount, Currency currency) {
        BankAccount bankAccount = validatorService.validateBankAccount(accountNumber);

        CurrencyBalance currencyBalance = validatorService.validateCurrencyBalance(bankAccount, currency, amount, TransactionType.DEPOSIT);
        if (Objects.isNull(currencyBalance.getAccount())) {
            currencyBalance.setAccount(bankAccount);
            currencyBalance.setCurrency(currency);
            currencyBalance.setCreatedDate(LocalDateTime.now());
            currencyBalance.setBalance(amount);
        } else {
            currencyBalance.setBalance(currencyBalance.getBalance().add(amount));
        }
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        currencyTransaction.setAccount(bankAccount);
        currencyTransaction.setAmount(amount);
        currencyTransaction.setCurrency(currency);
        currencyTransaction.setTransactionType(TransactionType.DEPOSIT);
        currencyTransaction.setCreatedDate(LocalDateTime.now());
        bankAccount.getTransactions().add(currencyTransaction);

        balanceRepository.save(currencyBalance);
        transactionRepository.save(currencyTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(String accountNumber, BigDecimal amount, Currency currency) {
        validatorService.checkStatus();

        BankAccount bankAccount = validatorService.validateBankAccount(accountNumber);

        CurrencyBalance currencyBalance = validatorService.validateCurrencyBalance(bankAccount, currency, amount, TransactionType.WITHDRAW);
        if (Objects.isNull(currencyBalance.getAccount())) {
            currencyBalance.setAccount(bankAccount);
            currencyBalance.setCurrency(currency);
            currencyBalance.setCreatedDate(LocalDateTime.now());
        }
        currencyBalance.setBalance(currencyBalance.getBalance().subtract(amount));

        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        currencyTransaction.setAccount(bankAccount);
        currencyTransaction.setAmount(amount);
        currencyTransaction.setCurrency(currency);
        currencyTransaction.setTransactionType(TransactionType.WITHDRAW);
        currencyTransaction.setCreatedDate(LocalDateTime.now());

        balanceRepository.save(currencyBalance);
        transactionRepository.save(currencyTransaction);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<Currency, BigDecimal> balance(String accountNumber) {
        BankAccount bankAccount = validatorService.validateBankAccount(accountNumber);

        return bankAccount.getBalances().stream()
                .collect(Collectors.toMap(CurrencyBalance::getCurrency, CurrencyBalance::getBalance));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BigDecimal exchange(String accountNumber, BigDecimal amount, Currency from, Currency to) {
        BankAccount bankAccount = validatorService.validateBankAccount(accountNumber);

        CurrencyBalance currencyFromBalance = validatorService.validateCurrencyBalance(bankAccount, from, amount, TransactionType.WITHDRAW);

        BigDecimal exchangedAmount = service.exchangeCurrency(amount, from, to);
        currencyFromBalance.setBalance(currencyFromBalance.getBalance().subtract(amount));

        CurrencyBalance currencyToBalance = validatorService.validateCurrencyBalance(bankAccount, to, amount, TransactionType.DEPOSIT);
        if (Objects.isNull(currencyToBalance.getAccount())) {
            currencyToBalance.setAccount(bankAccount);
            currencyToBalance.setCurrency(to);
            currencyToBalance.setCreatedDate(LocalDateTime.now());
            currencyToBalance.setBalance(amount);
        } else {
            currencyToBalance.setBalance(currencyToBalance.getBalance().add(amount));
        }

        validatorService.checkStatus();
        CurrencyTransaction withdrawMoneyTransaction = new CurrencyTransaction();
        withdrawMoneyTransaction.setAccount(bankAccount);
        withdrawMoneyTransaction.setAmount(amount);
        withdrawMoneyTransaction.setCurrency(from);
        withdrawMoneyTransaction.setTransactionType(TransactionType.WITHDRAW);
        withdrawMoneyTransaction.setCreatedDate(LocalDateTime.now());

        CurrencyTransaction depositMoneyTransaction = new CurrencyTransaction();
        depositMoneyTransaction.setAccount(bankAccount);
        depositMoneyTransaction.setAmount(exchangedAmount);
        depositMoneyTransaction.setCurrency(to);
        depositMoneyTransaction.setTransactionType(TransactionType.DEPOSIT);
        depositMoneyTransaction.setCreatedDate(LocalDateTime.now());

        balanceRepository.save(currencyFromBalance);
        balanceRepository.save(currencyToBalance);
        transactionRepository.save(withdrawMoneyTransaction);
        transactionRepository.save(depositMoneyTransaction);
        return exchangedAmount;
    }

}

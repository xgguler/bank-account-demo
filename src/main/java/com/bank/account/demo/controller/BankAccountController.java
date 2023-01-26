package com.bank.account.demo.controller;

import com.bank.account.demo.dto.*;
import com.bank.account.demo.model.Currency;
import com.bank.account.demo.service.BankAccountService;
import com.bank.account.demo.validator.ValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/account")
@Validated
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final ValidatorService service;

    public BankAccountController(BankAccountService bankAccountService, ValidatorService service) {
        this.bankAccountService = bankAccountService;
        this.service = service;
    }

    @PostMapping(value = "/deposit", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) {
        service.validateDepositRequest(request);
        bankAccountService.deposit(
                request.getAccountNumber(),
                request.getAmount(),
                request.getCurrency());
        return new ResponseEntity<>("Deposit applied successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/withdraw", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request) {
        service.validateWithdrawRequest(request);
        bankAccountService.withdraw(
                request.getAccountNumber(),
                request.getAmount(),
                request.getCurrency());
        return new ResponseEntity<>("Withdraw applied successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/balance", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<Currency, BigDecimal>> balance(@RequestParam String accountNumber) {
        service.validateBalanceRequest(accountNumber);
        return new ResponseEntity<>(bankAccountService.balance(accountNumber), HttpStatus.OK);
    }

    @PostMapping(value = "/exchange", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExchangeResponse> exchange(@RequestBody ExchangeRequest request) {
        service.validateExchangeRequest(request);
        BigDecimal amount = bankAccountService.exchange(
                request.getAccountNumber(),
                request.getAmount(),
                request.getFrom(),
                request.getTo()
        );
        ExchangeResponse response = new ExchangeResponse();
        response.setAccountNumber(request.getAccountNumber());
        response.setConvertedAmount(amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.bank.account.demo.controller;

import com.bank.account.demo.Application;
import com.bank.account.demo.dto.DepositRequest;
import com.bank.account.demo.dto.ExchangeRequest;
import com.bank.account.demo.dto.WithdrawRequest;
import com.bank.account.demo.model.*;
import com.bank.account.demo.repository.BankAccountRepository;
import com.bank.account.demo.repository.CurrencyBalanceRepository;
import com.bank.account.demo.repository.CurrencyTransactionRepository;
import com.bank.account.demo.service.BankAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "wiremock")
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class BankAccountControllerIT {

    private static WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountService service;

    @MockBean
    private BankAccountRepository repository;

    @MockBean
    private CurrencyBalanceRepository currencyBalanceRepository;

    @MockBean
    private CurrencyTransactionRepository currencyTransactionRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(
                new WireMockConfiguration()
                        .port(7070)
        );
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void clearWireMock() {
        wireMockServer.resetAll();
    }

    @Test
    void testWireMock() {
        assertTrue(wireMockServer.isRunning());
    }

    @Test
    void createDeposit() throws Exception {
        DepositRequest request = setDepositRequest();
        BankAccount account = setBankAccount(TransactionType.DEPOSIT);

        Mockito.when(repository.findAccountByAccountNumber(account.getAccountNumber())).thenReturn(account);

        mockMvc.perform(post("/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Basic " + "dXNlcjpwYXNzd29yZA==")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    @Test
    void createWithdraw() throws Exception {
        WithdrawRequest request = setWithdrawRequest();
        BankAccount account = setBankAccount(TransactionType.WITHDRAW);

        Mockito.when(repository.findAccountByAccountNumber(account.getAccountNumber())).thenReturn(account);

        mockCallLogRequest();

        mockMvc.perform(post("/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Basic " + "dXNlcjpwYXNzd29yZA==")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    @Test
    void getBalance() throws Exception {
        BankAccount account = setBankAccount(TransactionType.DEPOSIT);
        String accountNumber = "test";

        Mockito.when(repository.findAccountByAccountNumber(account.getAccountNumber())).thenReturn(account);

        mockMvc.perform(get("/account/balance?accountNumber=" + accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + "dXNlcjpwYXNzd29yZA==")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    @Test
    void createExchange() throws Exception {
        ExchangeRequest request = setExchangeRequest();
        BankAccount account = setBankAccount(TransactionType.DEPOSIT);

        Mockito.when(repository.findAccountByAccountNumber(account.getAccountNumber())).thenReturn(account);

        mockCallLogRequest();
        mockMvc.perform(post("/account/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Basic " + "dXNlcjpwYXNzd29yZA==")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk());
    }

    private void mockCallLogRequest() {
        wireMockServer.stubFor(WireMock.get(
                "/200"
        ).willReturn(
                aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(OK.value())
        ));
    }

    private DepositRequest setDepositRequest() {
        DepositRequest request = new DepositRequest();
        request.setAccountNumber("test");
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(Currency.EUR);

        return request;
    }

    private WithdrawRequest setWithdrawRequest() {
        WithdrawRequest request = new WithdrawRequest();
        request.setAccountNumber("test");
        request.setAmount(BigDecimal.ONE);
        request.setCurrency(Currency.EUR);

        return request;
    }

    private ExchangeRequest setExchangeRequest() {
        ExchangeRequest request = new ExchangeRequest();
        request.setAccountNumber("test");
        request.setAmount(BigDecimal.ONE);
        request.setFrom(Currency.EUR);
        request.setTo(Currency.USD);

        return request;
    }

    private BankAccount setBankAccount(TransactionType type) {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setAccountNumber("test");
        account.setEmail("test@mail.com");
        account.setCreatedDate(LocalDateTime.now());

        CurrencyBalance balance = new CurrencyBalance();
        balance.setBalance(BigDecimal.TEN);
        balance.setAccount(account);
        balance.setCurrency(Currency.EUR);
        balance.setCreatedDate(LocalDateTime.now());

        List<CurrencyBalance> currencyBalances = new ArrayList<>();
        currencyBalances.add(balance);

        CurrencyTransaction transaction = new CurrencyTransaction();
        transaction.setTransactionType(type);
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.ONE);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setCurrency(Currency.EUR);

        List<CurrencyTransaction> currencyTransactions = new ArrayList<>();
        currencyTransactions.add(transaction);

        account.setTransactions(currencyTransactions);
        account.setBalances(currencyBalances);
        return account;
    }
}

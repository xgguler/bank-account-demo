package com.bank.account.demo.dto;

import com.bank.account.demo.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("amount")
    private BigDecimal amount;
}

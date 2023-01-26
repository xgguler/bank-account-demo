package com.bank.account.demo.dto;

import com.bank.account.demo.model.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRequest {

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("from")
    private Currency from;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("to")
    private Currency to;
}

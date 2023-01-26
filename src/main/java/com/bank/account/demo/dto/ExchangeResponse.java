package com.bank.account.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeResponse {

    @JsonProperty("accountNumber")
    private String accountNumber;

    @JsonProperty("convertedAmount")
    private BigDecimal convertedAmount;
}

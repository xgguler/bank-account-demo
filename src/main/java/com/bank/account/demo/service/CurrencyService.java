package com.bank.account.demo.service;

import com.bank.account.demo.model.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {

    private final Map<Currency, BigDecimal> rates;

    public CurrencyService() {
        this.rates = new HashMap<>();
        rates.put(Currency.EUR, BigDecimal.valueOf(1));
        rates.put(Currency.USD, BigDecimal.valueOf(1));
        rates.put(Currency.SEK, BigDecimal.valueOf(1));
        rates.put(Currency.RUB, BigDecimal.valueOf(1));
    }

    public BigDecimal exchangeCurrency(BigDecimal amount, Currency from, Currency to) {
        if (from == Currency.EUR) return amount;

        BigDecimal exchangeRate = rates.get(to).divide(rates.get(from));
        return amount.multiply(exchangeRate).setScale(2);
    }
}

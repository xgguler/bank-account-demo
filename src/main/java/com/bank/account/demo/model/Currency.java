package com.bank.account.demo.model;

public enum Currency {
    EUR("EUR"), USD("USD"), SEK("SEK"), RUB("RUB");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

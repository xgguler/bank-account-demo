package com.bank.account.demo.model;

public enum TransactionType {
    DEPOSIT("DEPOSIT"), WITHDRAW("WITHDRAW");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package com.bank.account.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "currency_balance")
public class CurrencyBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}

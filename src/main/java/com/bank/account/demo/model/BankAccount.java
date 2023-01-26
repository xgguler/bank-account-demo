package com.bank.account.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    private String email;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<CurrencyBalance> balances;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<CurrencyTransaction> transactions;
}

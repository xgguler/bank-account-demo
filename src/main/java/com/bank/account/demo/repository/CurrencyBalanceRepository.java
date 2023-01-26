package com.bank.account.demo.repository;

import com.bank.account.demo.model.CurrencyBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyBalanceRepository extends JpaRepository<CurrencyBalance, Long> {

}

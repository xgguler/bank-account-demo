package com.bank.account.demo.repository;

import com.bank.account.demo.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Transactional
    @Query(value = "select * from BANK_ACCOUNT where ACCOUNT_NUMBER= :accountNumber", nativeQuery = true)
    BankAccount findAccountByAccountNumber(@Param("accountNumber") String accountNumber);
}

package com.accounting.accounting_backend.repository;

import com.accounting.accounting_backend.model.Transaction;
import com.accounting.accounting_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}

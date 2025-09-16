package com.accounting.accounting_backend.service;

import com.accounting.accounting_backend.model.Transaction;
import com.accounting.accounting_backend.model.User;
import com.accounting.accounting_backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction save(Transaction tx) {
        return transactionRepository.save(tx);
    }

    @Transactional
    public List<Transaction> getByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    @Transactional
    public Transaction update(Long id, User user, Transaction txDetails) {
        Transaction existingTx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        if (!existingTx.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You can only update your own transactions");
        }

        if (txDetails.getAmount() != null) {
            existingTx.setAmount(txDetails.getAmount());
        }
        if (txDetails.getCategory() != null) {
            existingTx.setCategory(txDetails.getCategory());
        }
        if (txDetails.getDate() != null) {
            existingTx.setDate(txDetails.getDate());
        }
        if (txDetails.getNote() != null) {
            existingTx.setNote(txDetails.getNote());
        }

        return transactionRepository.save(existingTx);
    }

    @Transactional
    public void delete(Long id, User user) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!tx.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        transactionRepository.delete(tx);
    }
}

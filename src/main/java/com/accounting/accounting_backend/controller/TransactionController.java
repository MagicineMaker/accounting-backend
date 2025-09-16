package com.accounting.accounting_backend.controller;

import com.accounting.accounting_backend.model.Transaction;
import com.accounting.accounting_backend.model.User;
import com.accounting.accounting_backend.repository.UserRepository;
import com.accounting.accounting_backend.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Transaction addTransaction(
            @RequestBody Transaction tx,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        tx.setUser(user);
        return transactionService.save(tx);
    }

    @GetMapping
    public List<Transaction> listTransactions(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        return transactionService.getByUser(user);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction tx,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        return transactionService.update(id, user, tx);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        transactionService.delete(id, user);
    }
}


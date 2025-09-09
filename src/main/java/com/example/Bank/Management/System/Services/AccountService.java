// src/main/java/com/example/Bank/Management/System/Services/AccountService.java

package com.example.Bank.Management.System.Services;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method to generate a unique 10-digit account number
    private String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();
        do {
            // Generate a random 10-digit number
            accountNumber = String.format("%010d", random.nextLong() % 10000000000L);
        } while (accountRepository.existsByAccountNumber(accountNumber)); // Ensure it's unique
        return accountNumber;
    }

    // Method to create a new account for a user
    public Account createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(0.0); // Set starting balance to zero
        return accountRepository.save(account);
    }

    // New method for depositing money
    public Account deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    // New method for withdrawing money
    public Account withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getBalance() < amount) {
            throw new IllegalStateException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }
}
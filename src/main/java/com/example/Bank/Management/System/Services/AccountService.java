// src/main/java/com/example/Bank/Management/System/Services/AccountService.java

package com.example.Bank.Management.System.Services;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Entity.Transaction;
import com.example.Bank.Management.System.Repository.AccountRepository;
import com.example.Bank.Management.System.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Method to generate a unique 10-digit account number
    private String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();
        do {
            accountNumber = String.format("%010d", random.nextLong() % 10000000000L);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    // Method to create a new account for a user
    public Account createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setBalance(0.0);
        return accountRepository.save(account);
    }

    // Helper method to save a transaction record
    private void logTransaction(String accountNumber, double amount, String type, String relatedAccount) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setRelatedAccount(relatedAccount);
        transactionRepository.save(transaction);
    }

    @Transactional
    public Account deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);

        logTransaction(accountNumber, amount, "CREDIT", null);

        return updatedAccount;
    }

    @Transactional
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
        Account updatedAccount = accountRepository.save(account);

        logTransaction(accountNumber, amount, "DEBIT", null);

        return updatedAccount;
    }

    // New method for transferring money between accounts
    @Transactional
    public void transfer(String sourceAccountNumber, String destinationAccountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }

        // Debit the source account
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalStateException("Insufficient funds in source account");
        }

        // Credit the destination account
        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Log transactions for both debit and credit
        logTransaction(sourceAccountNumber, amount, "DEBIT", destinationAccountNumber);
        logTransaction(destinationAccountNumber, amount, "CREDIT", sourceAccountNumber);
    }
}
package com.example.Bank.Management.System.Services;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Entity.Transaction;
import com.example.Bank.Management.System.Entity.User;
import com.example.Bank.Management.System.Repository.AccountRepository;
import com.example.Bank.Management.System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    public Account createAccount(Long userId, String accountType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account newAccount = new Account();
        newAccount.setAccountType(accountType);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setUser(user);

        return accountRepository.save(newAccount);
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be a positive number");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount.doubleValue());
        transaction.setType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());

        return transactionService.saveTransaction(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be a positive number");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount.doubleValue());
        transaction.setType("WITHDRAWAL");
        transaction.setTimestamp(LocalDateTime.now());

        return transactionService.saveTransaction(transaction);
    }

    @Transactional
    public Map<String, Transaction> transfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new RuntimeException("Source and destination accounts cannot be the same");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be a positive number");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in source account");
        }

        // Perform the transfer
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Create transaction records for both accounts
        Transaction sourceTransaction = new Transaction();
        sourceTransaction.setAccountNumber(sourceAccountNumber);
        sourceTransaction.setAmount(amount.doubleValue());
        sourceTransaction.setType("TRANSFER_OUT");
        sourceTransaction.setRelatedAccount(destinationAccountNumber);
        sourceTransaction.setTimestamp(LocalDateTime.now());
        transactionService.saveTransaction(sourceTransaction);

        Transaction destTransaction = new Transaction();
        destTransaction.setAccountNumber(destinationAccountNumber);
        destTransaction.setAmount(amount.doubleValue());
        destTransaction.setType("TRANSFER_IN");
        destTransaction.setRelatedAccount(sourceAccountNumber);
        destTransaction.setTimestamp(LocalDateTime.now());
        transactionService.saveTransaction(destTransaction);

        Map<String, Transaction> transactions = new HashMap<>();
        transactions.put("source", sourceTransaction);
        transactions.put("destination", destTransaction);

        return transactions;
    }
}
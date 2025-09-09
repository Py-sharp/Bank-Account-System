// src/main/java/com/example/Bank/Management/System/Controller/AccountController.java

package com.example.Bank.Management.System.Controller;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestParam Long userId) {
        try {
            Account newAccount = accountService.createAccount(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Account created successfully");
            response.put("accountId", newAccount.getId());
            response.put("accountNumber", newAccount.getAccountNumber());
            response.put("userId", newAccount.getUserId());
            response.put("balance", newAccount.getBalance());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create account: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        try {
            Account updatedAccount = accountService.deposit(request.getAccountNumber(), request.getAmount());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Deposit successful");
            response.put("accountNumber", updatedAccount.getAccountNumber());
            response.put("newBalance", updatedAccount.getBalance());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to deposit: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        try {
            Account updatedAccount = accountService.withdraw(request.getAccountNumber(), request.getAmount());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Withdrawal successful");
            response.put("accountNumber", updatedAccount.getAccountNumber());
            response.put("newBalance", updatedAccount.getBalance());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to withdraw: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // New endpoint for transfers
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            accountService.transfer(
                    request.getSourceAccountNumber(),
                    request.getDestinationAccountNumber(),
                    request.getAmount());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Transfer successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to transfer: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // Inner class to handle deposit and withdraw requests
    public static class TransactionRequest {
        private String accountNumber;
        private double amount;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    // New inner class to handle transfer requests
    public static class TransferRequest {
        private String sourceAccountNumber;
        private String destinationAccountNumber;
        private double amount;

        public String getSourceAccountNumber() {
            return sourceAccountNumber;
        }

        public void setSourceAccountNumber(String sourceAccountNumber) {
            this.sourceAccountNumber = sourceAccountNumber;
        }

        public String getDestinationAccountNumber() {
            return destinationAccountNumber;
        }

        public void setDestinationAccountNumber(String destinationAccountNumber) {
            this.destinationAccountNumber = destinationAccountNumber;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
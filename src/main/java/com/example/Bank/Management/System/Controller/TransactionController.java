// src/main/java/com/example/Bank/Management/System/Controller/TransactionController.java
package com.example.Bank.Management.System.Controller;

import com.example.Bank.Management.System.Entity.Transaction;
import com.example.Bank.Management.System.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin("*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(@RequestParam String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
            return ResponseEntity.ok(Map.of("transactions", transactions));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/slip")
    public ResponseEntity<?> getTransactionSlip(@RequestParam String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
            return ResponseEntity.ok(Map.of("transactions", transactions));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
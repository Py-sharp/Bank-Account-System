package com.example.Bank.Management.System.Controller;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin("*") // Enable CORS for the frontend
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestParam Long userId, @RequestParam String accountType) {
        try {
            Account newAccount = accountService.createAccount(userId, accountType);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Account created successfully");
            response.put("accountNumber", newAccount.getAccountNumber());
            response.put("accountType", newAccount.getAccountType());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        try {
            accountService.deposit(accountNumber, amount);
            return ResponseEntity.ok(Map.of("message", "Deposit successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        try {
            accountService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(Map.of("message", "Withdrawal successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam String sourceAccountNumber,
            @RequestParam String destinationAccountNumber, @RequestParam BigDecimal amount) {
        try {
            accountService.transfer(sourceAccountNumber, destinationAccountNumber, amount);
            return ResponseEntity.ok(Map.of("message", "Transfer successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

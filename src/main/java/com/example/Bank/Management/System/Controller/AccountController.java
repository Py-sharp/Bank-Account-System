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
}
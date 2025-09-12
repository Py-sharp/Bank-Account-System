package com.example.Bank.Management.System.Controller;

import com.example.Bank.Management.System.Entity.Budget;
import com.example.Bank.Management.System.Entity.BudgetSettings;
import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Services.BudgetService;
import com.example.Bank.Management.System.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin("*")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list-account")
    public ResponseEntity<?> getAccountBudgets(@RequestParam Long accountId) {
        try {
            Account account = accountService.findByAccountId(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            List<Budget> budgets = budgetService.getAccountBudgets(account);
            return ResponseEntity.ok(Map.of("budgets", budgets));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/create-account")
    public ResponseEntity<?> createBudgetForAccount(@RequestParam Long accountId, @RequestParam String name, @RequestParam BigDecimal targetAmount) {
        try {
            Account account = accountService.findByAccountId(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            Budget budget = budgetService.createBudget(account, name, targetAmount);
            return ResponseEntity.ok(Map.of(
                "message", "Budget created successfully",
                "budget", budget
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/update/{budgetId}")
    public ResponseEntity<?> updateBudget(@PathVariable Long budgetId,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) BigDecimal targetAmount,
                                          @RequestParam(required = false) Boolean isActive) {
        try {
            Budget updatedBudget = budgetService.updateBudget(budgetId, name, targetAmount, isActive);
            return ResponseEntity.ok(Map.of(
                "message", "Budget updated successfully",
                "budget", updatedBudget
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/settings-account")
    public ResponseEntity<?> getBudgetSettingsForAccount(@RequestParam Long accountId) {
        try {
            Account account = accountService.findByAccountId(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            BudgetSettings settings = budgetService.getBudgetSettings(account);
            return ResponseEntity.ok(Map.of("settings", settings));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/settings-account")
    public ResponseEntity<?> updateBudgetSettingsForAccount(@RequestParam Long accountId,
                                                 @RequestParam Boolean autoAllocationEnabled) {
        try {
            Account account = accountService.findByAccountId(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            BudgetSettings settings = budgetService.updateBudgetSettings(account, autoAllocationEnabled);
            return ResponseEntity.ok(Map.of(
                "message", "Budget settings updated successfully",
                "settings", settings
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
package com.example.Bank.Management.System.Services;

import com.example.Bank.Management.System.Entity.Account;
import com.example.Bank.Management.System.Entity.Budget;
import com.example.Bank.Management.System.Entity.BudgetSettings;
import com.example.Bank.Management.System.Repository.BudgetRepository;
import com.example.Bank.Management.System.Repository.BudgetSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetSettingsRepository budgetSettingsRepository;

    public List<Budget> getAccountBudgets(Account account) {
        return budgetRepository.findByAccount(account);
    }
    
    public Optional<Budget> getBudgetByName(Account account, String name) {
        return budgetRepository.findByAccountAndName(account, name);
    }

    public Budget createBudget(Account account, String name, BigDecimal targetAmount) {
        if (budgetRepository.findByAccountAndName(account, name).isPresent()) {
            throw new RuntimeException("A budget with this name already exists for this account.");
        }
        Budget budget = new Budget();
        budget.setAccount(account);
        budget.setName(name);
        budget.setTargetAmount(targetAmount);
        budget.setCurrentAmount(BigDecimal.ZERO);
        budget.setActive(true);
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Long budgetId, String name, BigDecimal targetAmount, Boolean isActive) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (name != null) budget.setName(name);
        if (targetAmount != null) budget.setTargetAmount(targetAmount);
        if (isActive != null) budget.setActive(isActive);

        return budgetRepository.save(budget);
    }

    @Transactional
    public void allocateFunds(Account account, BigDecimal amount) {
        BudgetSettings settings = budgetSettingsRepository.findByAccount(account)
                .orElseGet(() -> {
                    BudgetSettings newSettings = new BudgetSettings();
                    newSettings.setAccount(account);
                    newSettings.setAutoAllocationEnabled(false);
                    return budgetSettingsRepository.save(newSettings);
                });

        if (!settings.isAutoAllocationEnabled()) {
            return;
        }

        List<Budget> budgets = budgetRepository.findByAccount(account);

        Budget needsBudget = budgets.stream()
                .filter(b -> "Needs".equals(b.getName()))
                .findFirst()
                .orElseGet(() -> createBudget(account, "Needs", BigDecimal.valueOf(10000)));

        Budget wantsBudget = budgets.stream()
                .filter(b -> "Wants".equals(b.getName()))
                .findFirst()
                .orElseGet(() -> createBudget(account, "Wants", BigDecimal.valueOf(10000)));

        Budget savingsBudget = budgets.stream()
                .filter(b -> "Savings".equals(b.getName()))
                .findFirst()
                .orElseGet(() -> createBudget(account, "Savings", BigDecimal.valueOf(10000)));

        BigDecimal needsAllocation = amount.multiply(BigDecimal.valueOf(0.70));
        BigDecimal wantsAllocation = amount.multiply(BigDecimal.valueOf(0.20));
        BigDecimal savingsAllocation = amount.multiply(BigDecimal.valueOf(0.10));

        if (needsBudget.getCurrentAmount().compareTo(needsBudget.getTargetAmount()) < 0) {
            BigDecimal needsRemaining = needsBudget.getTargetAmount().subtract(needsBudget.getCurrentAmount());
            BigDecimal needsToAdd = needsAllocation.min(needsRemaining);
            needsBudget.setCurrentAmount(needsBudget.getCurrentAmount().add(needsToAdd));
            budgetRepository.save(needsBudget);
        }

        if (wantsBudget.getCurrentAmount().compareTo(wantsBudget.getTargetAmount()) < 0) {
            BigDecimal wantsRemaining = wantsBudget.getTargetAmount().subtract(wantsBudget.getCurrentAmount());
            BigDecimal wantsToAdd = wantsAllocation.min(wantsRemaining);
            wantsBudget.setCurrentAmount(wantsBudget.getCurrentAmount().add(wantsToAdd));
            budgetRepository.save(wantsBudget);
        }

        if (savingsBudget.getCurrentAmount().compareTo(savingsBudget.getTargetAmount()) < 0) {
            BigDecimal savingsRemaining = savingsBudget.getTargetAmount().subtract(savingsBudget.getCurrentAmount());
            BigDecimal savingsToAdd = savingsAllocation.min(savingsRemaining);
            savingsBudget.setCurrentAmount(savingsBudget.getCurrentAmount().add(savingsToAdd));
            budgetRepository.save(savingsBudget);
        }
    }

    public BudgetSettings getBudgetSettings(Account account) {
        return budgetSettingsRepository.findByAccount(account)
                .orElseGet(() -> {
                    BudgetSettings newSettings = new BudgetSettings();
                    newSettings.setAccount(account);
                    newSettings.setAutoAllocationEnabled(false);
                    return budgetSettingsRepository.save(newSettings);
                });
    }

    public BudgetSettings updateBudgetSettings(Account account, boolean autoAllocationEnabled) {
        BudgetSettings settings = getBudgetSettings(account);
        settings.setAutoAllocationEnabled(autoAllocationEnabled);
        return budgetSettingsRepository.save(settings);
    }
}
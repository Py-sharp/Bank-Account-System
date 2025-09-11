package com.example.Bank.Management.System.Repository;

import com.example.Bank.Management.System.Entity.BudgetSettings;
import com.example.Bank.Management.System.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetSettingsRepository extends JpaRepository<BudgetSettings, Long> {
    Optional<BudgetSettings> findByAccount(Account account);
}
package com.example.Bank.Management.System.Repository;

import com.example.Bank.Management.System.Entity.Budget;
import com.example.Bank.Management.System.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByAccount(Account account);
    Optional<Budget> findByAccountAndName(Account account, String name);
}
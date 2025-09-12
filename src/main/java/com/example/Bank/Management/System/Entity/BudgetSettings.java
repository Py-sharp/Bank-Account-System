package com.example.Bank.Management.System.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "budget_settings")
public class BudgetSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private boolean autoAllocationEnabled;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public boolean isAutoAllocationEnabled() { return autoAllocationEnabled; }
    public void setAutoAllocationEnabled(boolean autoAllocationEnabled) {
        this.autoAllocationEnabled = autoAllocationEnabled;
    }
}
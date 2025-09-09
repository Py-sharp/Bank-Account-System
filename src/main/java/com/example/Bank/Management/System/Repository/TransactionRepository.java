// src/main/java/com/example/Bank/Management/System/Repository/TransactionRepository.java

package com.example.Bank.Management.System.Repository;

import com.example.Bank.Management.System.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
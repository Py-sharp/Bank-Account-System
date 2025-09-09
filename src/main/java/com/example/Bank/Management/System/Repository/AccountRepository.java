// src/main/java/com/example/Bank/Management/System/Repository/AccountRepository.java

package com.example.Bank.Management.System.Repository;

import com.example.Bank.Management.System.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber); // ADD THIS LINE
}
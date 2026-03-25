package com.egonzalias.account.repository;



import com.egonzalias.account.domain.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findByAccountIdAndDateBetween(
            Long accountId,
            LocalDateTime start,
            LocalDateTime end
    );
}


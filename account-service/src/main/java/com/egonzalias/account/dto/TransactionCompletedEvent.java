package com.egonzalias.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionCompletedEvent(
        Long customerId,
        String accountNumber,
        String type,              // DEPOSIT / WITHDRAWAL
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime occurredAt
) {}

package com.egonzalias.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String accountNumber,
        String type,
        BigDecimal amount,
        BigDecimal balanceAfterTransaction,
        LocalDateTime date
) {}

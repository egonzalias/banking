package com.egonzalias.account.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDetail(
        LocalDateTime date,
        String type,
        BigDecimal amount,
        BigDecimal balanceAfterTransaction
) {}


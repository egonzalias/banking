package com.egonzalias.account.dto;


import java.math.BigDecimal;

public record AccountBalanceResponse(
        String accountNumber,
        String accountType,
        BigDecimal currentBalance,
        Boolean active
) {}


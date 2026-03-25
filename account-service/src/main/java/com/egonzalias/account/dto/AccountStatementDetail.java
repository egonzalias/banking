package com.egonzalias.account.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountStatementDetail(
        String accountNumber,
        String accountType,
        BigDecimal currentBalance,
        List<TransactionDetail> transactions
) {}



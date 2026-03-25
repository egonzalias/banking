package com.egonzalias.customer.dto;


import java.util.List;

public record AccountStatementResponse(
        String customerName,
        String customerIdentification,
        List<AccountStatementDetail> accounts
) {}

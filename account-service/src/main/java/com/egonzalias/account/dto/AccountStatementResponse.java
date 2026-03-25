package com.egonzalias.account.dto;


import java.util.List;

public record AccountStatementResponse(
        Long customerId,
        List<AccountStatementDetail> accounts
) {}


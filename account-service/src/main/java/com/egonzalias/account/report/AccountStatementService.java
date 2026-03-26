package com.egonzalias.account.report;

import com.egonzalias.account.dto.AccountStatementResponse;

import java.time.LocalDate;

public interface AccountStatementService {
    public AccountStatementResponse generateReport(
            Long customerId,
            LocalDate from,
            LocalDate to
    );
}

package com.egonzalias.account.controller;



import com.egonzalias.account.dto.AccountStatementResponse;
import com.egonzalias.account.report.AccountStatementService;
import com.egonzalias.account.report.impl.AccountStatementServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final AccountStatementService service;

    public ReportController(AccountStatementService service) {
        this.service = service;
    }


    @GetMapping
    public AccountStatementResponse getAccountStatement(
            @RequestParam Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return service.generateReport(customerId, from, to);
    }

}


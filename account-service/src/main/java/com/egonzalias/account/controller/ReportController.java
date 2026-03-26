package com.egonzalias.account.controller;



import com.egonzalias.account.dto.AccountStatementResponse;
import com.egonzalias.account.report.AccountStatementService;
import com.egonzalias.account.report.impl.AccountStatementServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private static final Logger log =
            LoggerFactory.getLogger(ReportController.class);

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
        log.info(
                "Account statement request received, customerId={}, from={}, to={}",
                customerId, from, to
        );
        return service.generateReport(customerId, from, to);
    }

}


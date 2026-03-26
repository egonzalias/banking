package com.egonzalias.account.controller;
import com.egonzalias.account.domain.Account;
import com.egonzalias.account.dto.AccountBalanceResponse;
import com.egonzalias.account.dto.CreateAccountRequest;
import com.egonzalias.account.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private static final Logger log =
            LoggerFactory.getLogger(AccountController.class);

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.info("Create account request received, accountNumber={}",
                request.accountNumber());

        Account created = service.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable("accountNumber") String accountNumber
    ) {
        log.info("Get account balance request received, accountNumber={}",
                accountNumber);

        AccountBalanceResponse response =
                service.getAccountBalance(accountNumber);

        return ResponseEntity.ok(response);
    }
}


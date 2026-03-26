package com.egonzalias.account.controller;



import com.egonzalias.account.domain.Account;
import com.egonzalias.account.dto.AccountBalanceResponse;
import com.egonzalias.account.dto.CreateAccountRequest;
import com.egonzalias.account.service.AccountService;
import com.egonzalias.account.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        Account created = service.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable("accountNumber") String accountNumber
    ) {
        AccountBalanceResponse response = service.getAccountBalance(accountNumber);
        return ResponseEntity.ok(response);
    }

}


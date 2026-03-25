package com.egonzalias.account.controller;



import com.egonzalias.account.domain.Account;
import com.egonzalias.account.dto.AccountBalanceResponse;
import com.egonzalias.account.dto.CreateAccountRequest;
import com.egonzalias.account.exception.AccountNotFoundException;
import com.egonzalias.account.repository.AccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setInitialBalance(request.initialBalance());
        account.setActive(request.active());
        account.setCustomerId(request.customerId());

        return ResponseEntity.status(201).body(repository.save(account));
    }


    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable("accountNumber") String accountNumber
    ) {
        Account account = repository.
                findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return ResponseEntity.ok(
                new AccountBalanceResponse(
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getCurrentBalance(),
                        account.getActive()
                )
        );
    }

}


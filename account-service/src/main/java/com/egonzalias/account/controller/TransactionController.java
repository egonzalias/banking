package com.egonzalias.account.controller;



import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.CreateTransactionRequest;
import com.egonzalias.account.dto.TransactionResponse;
import com.egonzalias.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final AccountService service;

    public TransactionController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        TransactionResponse response = service.registerTransaction(
                request.accountNumber(),
                request.amount(),
                request.type()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}




package com.egonzalias.account.controller;



import com.egonzalias.account.dto.CreateTransactionRequest;
import com.egonzalias.account.dto.TransactionResponse;
import com.egonzalias.account.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private static final Logger log =
            LoggerFactory.getLogger(TransactionController.class);

    private final AccountService service;

    public TransactionController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        log.info(
                "Create transaction request received, accountNumber={}, type={}, amount={}",
                request.accountNumber(),
                request.type(),
                request.amount()
        );

        TransactionResponse response = service.registerTransaction(
                request.accountNumber(),
                request.amount(),
                request.type()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}




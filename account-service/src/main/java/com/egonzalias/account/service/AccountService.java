package com.egonzalias.account.service;

import com.egonzalias.account.domain.Account;
import com.egonzalias.account.dto.AccountBalanceResponse;
import com.egonzalias.account.dto.CreateAccountRequest;
import com.egonzalias.account.dto.TransactionResponse;

import java.math.BigDecimal;

public interface AccountService {
    public Account createAccount(CreateAccountRequest request);
    public AccountBalanceResponse getAccountBalance(String accountNumber);
    public TransactionResponse registerTransaction(
            String accountNumber,
            BigDecimal amount,
            String type
    );
}

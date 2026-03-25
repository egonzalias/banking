package com.egonzalias.account.service;

import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.AccountBalanceResponse;
import com.egonzalias.account.dto.CreateAccountRequest;
import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.egonzalias.account.dto.TransactionResponse;
import com.egonzalias.account.exception.AccountAlreadyExistsException;
import com.egonzalias.account.exception.AccountNotFoundException;
import com.egonzalias.account.exception.InsufficientBalanceException;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher transactionEventPublisher;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository, TransactionEventPublisher transactionEventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionEventPublisher = transactionEventPublisher;
    }


    public Account createAccount(CreateAccountRequest request) {
        if (accountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            throw new AccountAlreadyExistsException(request.accountNumber());
        }

        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setInitialBalance(request.initialBalance());
        account.setCurrentBalance(request.initialBalance());
        account.setActive(request.active());
        account.setCustomerId(request.customerId());

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AccountBalanceResponse getAccountBalance(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return new AccountBalanceResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrentBalance(),
                account.getActive()
        );
    }


    public TransactionResponse registerTransaction(
            String accountNumber,
            BigDecimal amount,
            String type
    ) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        BigDecimal newBalance = account.getCurrentBalance().add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException();
        }

        account.setCurrentBalance(newBalance);

        AccountTransaction transaction = new AccountTransaction();
        transaction.setAccount(account);
        transaction.setDate(LocalDateTime.now());
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(newBalance);

        transactionRepository.save(transaction);
        accountRepository.save(account);
        transactionEventPublisher.publish(
                new TransactionCompletedEvent(
                        account.getCustomerId(),
                        account.getAccountNumber(),
                        type,
                        amount,
                        newBalance,
                        LocalDateTime.now()
                )
        );

        return new TransactionResponse(
                account.getAccountNumber(),
                type,
                amount,
                newBalance,
                transaction.getDate()
        );
    }

}



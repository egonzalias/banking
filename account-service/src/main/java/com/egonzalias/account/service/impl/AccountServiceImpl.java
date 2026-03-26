package com.egonzalias.account.service.impl;

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
import com.egonzalias.account.service.AccountService;
import com.egonzalias.account.service.TransactionEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger log =
            LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher transactionEventPublisher;

    public AccountServiceImpl(AccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              TransactionEventPublisher transactionEventPublisher) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionEventPublisher = transactionEventPublisher;
    }

    @Override
    public Account createAccount(CreateAccountRequest request) {
        log.info("Creating account, accountNumber={}", request.accountNumber());

        if (accountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            log.warn("Account already exists, accountNumber={}",
                    request.accountNumber());
            throw new AccountAlreadyExistsException(request.accountNumber());
        }

        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setInitialBalance(request.initialBalance());
        account.setCurrentBalance(request.initialBalance());
        account.setActive(request.active());
        account.setCustomerId(request.customerId());

        Account saved = accountRepository.save(account);

        log.info("Account created successfully, accountNumber={}",
                saved.getAccountNumber());

        return saved;
    }

    @Transactional(readOnly = true)
    @Override
    public AccountBalanceResponse getAccountBalance(String accountNumber) {
        log.info("Fetching account balance, accountNumber={}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found, accountNumber={}", accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });

        return new AccountBalanceResponse(
                account.getAccountNumber(),
                account.getAccountType(),
                account.getCurrentBalance(),
                account.getActive()
        );
    }

    @Override
    public TransactionResponse registerTransaction(
            String accountNumber,
            BigDecimal amount,
            String type
    ) {
        log.info("Registering transaction, accountNumber={}, type={}, amount={}",
                accountNumber, type, amount);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found during transaction, accountNumber={}",
                            accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });

        BigDecimal newBalance = account.getCurrentBalance().add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.warn(
                    "Insufficient balance, accountNumber={}, currentBalance={}, amount={}",
                    accountNumber, account.getCurrentBalance(), amount
            );
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

        log.info("Transaction completed successfully, accountNumber={}, newBalance={}",
                accountNumber, newBalance);

        return new TransactionResponse(
                account.getAccountNumber(),
                type,
                amount,
                newBalance,
                transaction.getDate()
        );
    }
}





package com.egonzalias.account.report.impl;



import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.AccountStatementDetail;
import com.egonzalias.account.dto.AccountStatementResponse;
import com.egonzalias.account.dto.TransactionDetail;
import com.egonzalias.account.report.AccountStatementService;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AccountStatementServiceImpl implements AccountStatementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountStatementServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountStatementResponse generateReport(
            Long customerId,
            LocalDate from,
            LocalDate to
    ) {
        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX);

        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        List<AccountStatementDetail> accountDetails = accounts.stream()
                .map(account -> {
                    List<AccountTransaction> transactions =
                            transactionRepository.findByAccountIdAndDateBetween(
                                    account.getId(),
                                    fromDateTime,
                                    toDateTime
                            );

                    return new AccountStatementDetail(
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getCurrentBalance(),
                            transactions.stream()
                                    .map(this::mapTransaction)
                                    .toList()
                    );
                })
                .toList();

        return new AccountStatementResponse(customerId, accountDetails);
    }


    private TransactionDetail mapTransaction(AccountTransaction tx) {
        return new TransactionDetail(
                tx.getDate(),
                tx.getTransactionType(),
                tx.getAmount(),
                tx.getBalanceAfterTransaction()
        );
    }
}


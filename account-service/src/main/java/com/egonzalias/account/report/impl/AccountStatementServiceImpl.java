package com.egonzalias.account.report.impl;



import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.AccountStatementDetail;
import com.egonzalias.account.dto.AccountStatementResponse;
import com.egonzalias.account.dto.TransactionDetail;
import com.egonzalias.account.report.AccountStatementService;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AccountStatementServiceImpl implements AccountStatementService {

    private static final Logger log =
            LoggerFactory.getLogger(AccountStatementServiceImpl.class);

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
        log.info(
                "Generating account statement, customerId={}, from={}, to={}",
                customerId, from, to
        );

        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX);

        List<Account> accounts =
                accountRepository.findByCustomerId(customerId);

        log.info(
                "Found {} accounts for customerId={}",
                accounts.size(), customerId
        );

        List<AccountStatementDetail> accountDetails = accounts.stream()
                .map(account -> {
                    List<AccountTransaction> transactions =
                            transactionRepository.findByAccountIdAndDateBetween(
                                    account.getId(),
                                    fromDateTime,
                                    toDateTime
                            );

                    log.debug(
                            "Account {} has {} transactions in period",
                            account.getAccountNumber(),
                            transactions.size()
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

        log.info(
                "Account statement generated successfully, customerId={}, accounts={}",
                customerId, accountDetails.size()
        );

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


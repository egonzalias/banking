package com.egonzalias.account.report;



import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.AccountStatementDetail;
import com.egonzalias.account.dto.AccountStatementResponse;
import com.egonzalias.account.dto.TransactionDetail;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountStatementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountStatementService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    public AccountStatementResponse generateReport(
            Long customerId,
            LocalDate from,
            LocalDate to
    ) {
        // ✅ Normalización correcta de fechas
        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.atTime(LocalTime.MAX);

        // ✅ SOLO cuentas del customer
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


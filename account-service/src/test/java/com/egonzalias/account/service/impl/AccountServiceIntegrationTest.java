package com.egonzalias.account.service.impl;

import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.egonzalias.account.dto.TransactionResponse;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import com.egonzalias.account.service.AccountService;
import com.egonzalias.account.service.TransactionEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionEventPublisher transactionEventPublisher;

    @Test
    void shouldRegisterTransactionAndPersistData() {
        // ---------- Arrange ----------
        Account account = new Account();
        account.setAccountNumber("ACC-100");
        account.setAccountType("DEPOSITO");
        account.setInitialBalance(new BigDecimal("1000.00"));
        account.setCurrentBalance(new BigDecimal("1000.00"));
        account.setActive(true);
        account.setCustomerId(1L);

        accountRepository.save(account);

        BigDecimal amount = new BigDecimal("-200.00");
        String type = "RETIRO";

        // ---------- Act ----------
        TransactionResponse response =
                accountService.registerTransaction(
                        "ACC-100",
                        amount,
                        type
                );

        // ---------- Assert ----------
        assertNotNull(response);
        assertEquals("ACC-100", response.accountNumber());
        assertEquals(type, response.type());
        assertEquals(amount, response.amount());
        assertEquals(
                new BigDecimal("800.00"),
                response.balanceAfterTransaction()
        );

        // ✅ Account persisted with updated balance
        Account updatedAccount =
                accountRepository.findByAccountNumber("ACC-100").orElseThrow();

        assertEquals(
                new BigDecimal("800.00"),
                updatedAccount.getCurrentBalance()
        );

        // ✅ Transaction persisted
        List<AccountTransaction> transactions =
                transactionRepository.findAll();

        assertEquals(1, transactions.size());

        AccountTransaction transaction = transactions.get(0);
        assertEquals(type, transaction.getTransactionType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(
                new BigDecimal("800.00"),
                transaction.getBalanceAfterTransaction()
        );

        // ✅ Event published
        verify(transactionEventPublisher)
                .publish(any(TransactionCompletedEvent.class));
    }
}

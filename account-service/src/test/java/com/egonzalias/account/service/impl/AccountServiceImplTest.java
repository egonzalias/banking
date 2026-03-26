package com.egonzalias.account.service.impl;

import com.egonzalias.account.domain.Account;
import com.egonzalias.account.domain.AccountTransaction;
import com.egonzalias.account.dto.TransactionCompletedEvent;
import com.egonzalias.account.dto.TransactionResponse;
import com.egonzalias.account.exception.AccountNotFoundException;
import com.egonzalias.account.exception.InsufficientBalanceException;
import com.egonzalias.account.repository.AccountRepository;
import com.egonzalias.account.repository.TransactionRepository;
import com.egonzalias.account.service.TransactionEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionEventPublisher transactionEventPublisher;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldRegisterTransactionSuccessfully() {
        // ---------- Arrange ----------
        String accountNumber = "ACC-123";
        BigDecimal initialBalance = new BigDecimal("500.00");
        BigDecimal amount = new BigDecimal("100.00");
        BigDecimal expectedBalance = new BigDecimal("600.00");
        String type = "DEPOSIT";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCurrentBalance(initialBalance);
        account.setCustomerId(1L);

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        // ---------- Act ----------
        TransactionResponse response =
                accountService.registerTransaction(accountNumber, amount, type);

        // ---------- Assert ----------
        assertNotNull(response);
        assertEquals(accountNumber, response.accountNumber());
        assertEquals(type, response.type());
        assertEquals(amount, response.amount());
        assertEquals(expectedBalance, response.balanceAfterTransaction());

        verify(transactionRepository).save(any(AccountTransaction.class));
        verify(accountRepository).save(account);
        verify(transactionEventPublisher)
                .publish(any(TransactionCompletedEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        // ---------- Arrange ----------
        String accountNumber = "NON_EXISTENT";
        BigDecimal amount = BigDecimal.TEN;
        String type = "DEPOSIT";

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        // ---------- Act & Assert ----------
        assertThrows(
                AccountNotFoundException.class,
                () -> accountService.registerTransaction(accountNumber, amount, type)
        );

        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionEventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        // ---------- Arrange ----------
        String accountNumber = "ACC-123";
        BigDecimal initialBalance = new BigDecimal("500.00");
        BigDecimal amount = new BigDecimal("-600.00");
        String type = "WITHDRAW";

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCurrentBalance(initialBalance);

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(account));

        // ---------- Act & Assert ----------
        assertThrows(
                InsufficientBalanceException.class,
                () -> accountService.registerTransaction(accountNumber, amount, type)
        );

        verify(transactionRepository, never()).save(any());
        verify(transactionEventPublisher, never()).publish(any());
    }
}
package com.egonzalias.account.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String accountNumber) {
        super("Account already exists with number: " + accountNumber);
    }
}


package com.egonzalias.customer.exception;


public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String identification) {
        super("Customer already exists with identification: " + identification);
    }
}


package com.egonzalias.customer.exception;

public class CustomerNotFoundException  extends RuntimeException {

    public CustomerNotFoundException(Long identification) {
        super("Customer not found: " + identification);
    }
}


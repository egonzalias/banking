package com.egonzalias.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;


public record CreateTransactionRequest(

        @NotBlank
        String accountNumber,

        @NotNull
        BigDecimal amount,

        @NotBlank
        String type

) {}



package com.egonzalias.account.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(

        @NotBlank
        String accountNumber,

        @NotBlank
        String accountType,

        @NotNull
        BigDecimal initialBalance,

        @NotNull
        Boolean active,

        @NotNull
        Long customerId

) {}


package com.egonzalias.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCustomerRequest(
        @NotBlank String name,
        @NotBlank String gender,
        @NotNull @Positive Integer age,
        @NotBlank String address,
        @NotBlank String phone,
        @NotNull Boolean active
) {}

package com.egonzalias.customer.service.impl;

import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.exception.CustomerAlreadyExistsException;
import com.egonzalias.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldCreateCustomerSuccessfully() {
        // ---------- Arrange ----------
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Juan Perez",
                "M",
                30,
                "1234567890",
                "Calle 123",
                "555-1234",
                "secret",
                true
        );

        when(repository.findByIdentification(request.identification()))
                .thenReturn(Optional.empty());

        when(repository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ---------- Act ----------
        Customer result = customerService.create(request);

        // ---------- Assert ----------
        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.gender(), result.getGender());
        assertEquals(request.age(), result.getAge());
        assertEquals(request.identification(), result.getIdentification());
        assertEquals(request.address(), result.getAddress());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.password(), result.getPassword());
        assertEquals(request.active(), result.getActive());

        verify(repository).findByIdentification(request.identification());
        verify(repository).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyExists() {
        // ---------- Arrange ----------
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Juan Perez",
                "M",
                30,
                "1234567890",
                "Calle 123",
                "555-1234",
                "secret",
                true
        );

        when(repository.findByIdentification(request.identification()))
                .thenReturn(Optional.of(new Customer()));

        // ---------- Act & Assert ----------
        assertThrows(
                CustomerAlreadyExistsException.class,
                () -> customerService.create(request)
        );

        verify(repository).findByIdentification(request.identification());
        verify(repository, never()).save(any());
    }
}

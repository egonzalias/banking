package com.egonzalias.customer.controller;


import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.dto.UpdateCustomerRequest;
import com.egonzalias.customer.service.CustomerService;
import com.egonzalias.customer.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        Customer created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        Customer updated = service.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("id") Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}


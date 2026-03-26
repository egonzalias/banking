package com.egonzalias.customer.controller;


import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.dto.UpdateCustomerRequest;
import com.egonzalias.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private static final Logger log =
            LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
        log.info("Create customer request received, identification={}",
                request.identification());

        Customer created = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable("id") Long id
    ) {
        log.info("Get customer by id request received, id={}", id);

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        log.info("Get all customers request received");

        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        log.info("Update customer request received, id={}", id);

        Customer updated = service.update(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("id") Long id
    ) {
        log.info("Delete customer request received, id={}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}


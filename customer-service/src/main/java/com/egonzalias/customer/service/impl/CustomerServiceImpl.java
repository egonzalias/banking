package com.egonzalias.customer.service.impl;


import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.dto.UpdateCustomerRequest;
import com.egonzalias.customer.exception.CustomerAlreadyExistsException;
import com.egonzalias.customer.exception.CustomerNotFoundException;
import com.egonzalias.customer.repository.CustomerRepository;
import com.egonzalias.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log =
            LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(CreateCustomerRequest request) {
        log.info("Creating customer, identification={}", request.identification());

        if (repository.findByIdentification(request.identification()).isPresent()) {
            log.warn("Customer already exists, identification={}",
                    request.identification());
            throw new CustomerAlreadyExistsException(request.identification());
        }

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setGender(request.gender());
        customer.setAge(request.age());
        customer.setIdentification(request.identification());
        customer.setAddress(request.address());
        customer.setPhone(request.phone());
        customer.setPassword(request.password());
        customer.setActive(request.active());

        Customer saved = repository.save(customer);

        log.info("Customer created successfully, id={}", saved.getCustomerId());

        return saved;
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(Long id) {
        log.info("Fetching customer by id={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer not found, id={}", id);
                    return new CustomerNotFoundException(id);
                });
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll() {
        log.info("Fetching all customers");
        return repository.findAll();
    }

    @Override
    public Customer update(Long id, UpdateCustomerRequest request) {
        log.info("Updating customer, id={}", id);

        Customer customer = getById(id);

        customer.setName(request.name());
        customer.setGender(request.gender());
        customer.setAge(request.age());
        customer.setAddress(request.address());
        customer.setPhone(request.phone());
        customer.setActive(request.active());

        Customer updated = repository.save(customer);

        log.info("Customer updated successfully, id={}", id);

        return updated;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting customer, id={}", id);

        Customer customer = getById(id);
        repository.delete(customer);

        log.info("Customer deleted successfully, id={}", id);
    }

}


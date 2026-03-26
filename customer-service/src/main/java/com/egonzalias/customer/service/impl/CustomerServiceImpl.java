package com.egonzalias.customer.service.impl;


import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.dto.UpdateCustomerRequest;
import com.egonzalias.customer.exception.CustomerAlreadyExistsException;
import com.egonzalias.customer.exception.CustomerNotFoundException;
import com.egonzalias.customer.repository.CustomerRepository;
import com.egonzalias.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {


    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer create(CreateCustomerRequest request) {

        if (repository.findByIdentification(request.identification()).isPresent()) {
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

        return repository.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @Override
    public Customer update(Long id, UpdateCustomerRequest request) {

        Customer customer = getById(id);

        customer.setName(request.name());
        customer.setGender(request.gender());
        customer.setAge(request.age());
        customer.setAddress(request.address());
        customer.setPhone(request.phone());
        customer.setActive(request.active());

        return repository.save(customer);
    }

    @Override
    public void delete(Long id) {
        Customer customer = getById(id);
        repository.delete(customer);
    }
}


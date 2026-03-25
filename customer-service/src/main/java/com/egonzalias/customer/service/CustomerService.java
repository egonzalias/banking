package com.egonzalias.customer.service;


import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.exception.ResourceNotFoundException;
import com.egonzalias.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    public Customer getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Customer update(Long id, Customer updated) {
        Customer existing = getById(id);

        existing.setName(updated.getName());
        existing.setGender(updated.getGender());
        existing.setAge(updated.getAge());
        existing.setIdentification(updated.getIdentification());
        existing.setAddress(updated.getAddress());
        existing.setPhone(updated.getPhone());
        existing.setPassword(updated.getPassword());
        existing.setActive(updated.getActive());

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }
}


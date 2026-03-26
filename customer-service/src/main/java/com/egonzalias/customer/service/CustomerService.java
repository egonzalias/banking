package com.egonzalias.customer.service;

import com.egonzalias.customer.domain.Customer;
import com.egonzalias.customer.dto.CreateCustomerRequest;
import com.egonzalias.customer.dto.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {
    public Customer create(CreateCustomerRequest request);
    public Customer getById(Long id);
    public List<Customer> getAll();
    public Customer update(Long id, UpdateCustomerRequest request);
    public void delete(Long id);
}

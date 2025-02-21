package com.brokagefirm.challange.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.brokagefirm.challange.models.Customer;
import com.brokagefirm.challange.models.CustomerType;
import com.brokagefirm.challange.repositories.CustomerRepository;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getAssetVendorCustomer() {
        Optional<Customer> customer = customerRepository.findByType(CustomerType.ADMIN);
        if (!customer.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Asset vendor customer not found");
        }
        return customer.get();
    }

    public Customer getCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Customer not found with id: " + id);
        }
        return customer.get();
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}

package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.exceptions.customer.CustomerAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByNameException;
import com.example.fooddeliverysystem.model.Customer;
import com.example.fooddeliverysystem.repo.CustomerRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepo customerRepo, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundByIdException(customerId));
        return customer;
    }

    @Override
    public Customer getCustomerByName(String customerName) {
        Customer customer = customerRepo.searchCustomerByName(customerName)
                .orElseThrow(() -> new CustomerNotFoundByNameException(customerName));
        return customer;
    }

    @Override
    public void addNewCustomer(Customer customer) {

        Optional<Customer> customerOptional = customerRepo.searchCustomerByEmail(customer.getEmail());

        customerOptional.ifPresentOrElse(
                (x) -> {
                    throw new CustomerAlreadyExistsException(x.getEmail());
                },
                () -> {
                    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                    customerRepo.save(customer);
                }
        );

    }

    @Override
    public void deleteCustomerById(Long customerId) {
        customerRepo.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundByIdException(customerId));

        customerRepo.deleteById(customerId);
    }


}

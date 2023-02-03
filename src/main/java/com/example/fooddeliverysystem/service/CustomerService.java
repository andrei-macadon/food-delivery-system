package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.Customer;

import java.util.List;

public interface CustomerService {
    void addNewCustomer(Customer customer);

    void deleteCustomerById(Long customerId);

    List<Customer> getAllCustomers();

    Customer getCustomerById(Long customerId);

    Customer getCustomerByName(String customerName);
}

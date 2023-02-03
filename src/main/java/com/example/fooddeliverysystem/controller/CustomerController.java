package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.Customer;
import com.example.fooddeliverysystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok()
                .body(customers);
    }

    @GetMapping("/id/{customer_id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customer_id") Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok()
                .body(customer);
    }

    @GetMapping("/{customer_name}")
    public ResponseEntity<Customer> getCustomerByName(@PathVariable("customer_name") String customerName) {
        Customer customer = customerService.getCustomerByName(customerName);
        return ResponseEntity.ok()
                .body(customer);
    }
    
    @PostMapping
    public ResponseEntity<String> addNewCustomer(@RequestBody @Valid Customer customer) {
        customerService.addNewCustomer(customer);
        return ResponseEntity.ok().body("The custome with the name " + customer.getName() + " has been added to the db.");
    }

    @DeleteMapping("/id/{customer_id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable("customer_id") Long customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.ok().body("The customer with the id " + customerId + " has been deleted.");
    }

}

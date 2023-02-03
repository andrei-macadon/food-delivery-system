package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByNameException;
import com.example.fooddeliverysystem.model.Customer;
import com.example.fooddeliverysystem.repo.CustomerRepo;
import com.example.fooddeliverysystem.model.SecurityCustomer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaUserDetailsService implements UserDetailsService {

    private final CustomerRepo customerRepo;

    public JpaUserDetailsService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Customer> customer = customerRepo.searchCustomerByName(username);
        return customer.map(SecurityCustomer::new)
                .orElseThrow(() -> new CustomerNotFoundByNameException(username));
    }
}

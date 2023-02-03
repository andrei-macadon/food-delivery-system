package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query("SELECT c from Customer c WHERE c.name = :name")
    Optional<Customer> searchCustomerByName(String name);
    @Query("SELECT c from Customer c WHERE c.email = :email")
    Optional<Customer> searchCustomerByEmail(String email);
}

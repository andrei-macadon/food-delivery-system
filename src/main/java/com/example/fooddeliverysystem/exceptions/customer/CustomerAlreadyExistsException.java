package com.example.fooddeliverysystem.exceptions.customer;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String email) {
        super("Customer with email " + email + " already exists.");
    }
}

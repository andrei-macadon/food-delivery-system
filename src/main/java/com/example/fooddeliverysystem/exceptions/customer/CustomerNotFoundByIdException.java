package com.example.fooddeliverysystem.exceptions.customer;

public class CustomerNotFoundByIdException extends RuntimeException {

    public CustomerNotFoundByIdException(Long customerId) {
        super("The customer with the id " + customerId + " has not been found.");
    }
}

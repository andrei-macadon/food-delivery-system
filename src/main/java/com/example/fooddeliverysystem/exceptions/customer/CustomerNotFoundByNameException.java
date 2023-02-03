package com.example.fooddeliverysystem.exceptions.customer;

public class CustomerNotFoundByNameException extends RuntimeException {
    public CustomerNotFoundByNameException(String name) {
        super("Customer with the name " + name + " has not been found");
    }
}

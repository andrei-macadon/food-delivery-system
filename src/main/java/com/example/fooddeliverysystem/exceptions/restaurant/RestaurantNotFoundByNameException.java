package com.example.fooddeliverysystem.exceptions.restaurant;

public class RestaurantNotFoundByNameException extends RuntimeException{

    public RestaurantNotFoundByNameException(String restaurantName) {
        super("The restaurant " + restaurantName + " was not found.");
    }
}

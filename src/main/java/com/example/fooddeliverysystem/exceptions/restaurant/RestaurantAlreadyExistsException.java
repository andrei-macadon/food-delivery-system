package com.example.fooddeliverysystem.exceptions.restaurant;

public class RestaurantAlreadyExistsException extends RuntimeException {

    public RestaurantAlreadyExistsException(String restaurantName) {
        super("The restaurant " + restaurantName + " already exists in the db.");
    }
}

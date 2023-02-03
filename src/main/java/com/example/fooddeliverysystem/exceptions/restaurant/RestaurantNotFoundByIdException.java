package com.example.fooddeliverysystem.exceptions.restaurant;

public class RestaurantNotFoundByIdException extends RuntimeException {

    public RestaurantNotFoundByIdException(Long restaurantId) {
        super("The restaurant with the " + restaurantId + " id has not been found.");
    }
}

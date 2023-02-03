package com.example.fooddeliverysystem.exceptions.city;

public class CityNotFoundByIdException extends RuntimeException{

    public CityNotFoundByIdException(Long cityId) {
        super("The city with the id: " + cityId + " has not been found.");
    }
}

package com.example.fooddeliverysystem.exceptions.city;

public class CityAlreadyExistsException extends RuntimeException {

    public CityAlreadyExistsException(String cityName) {
        super("City " + cityName + " already exists in the db.");
    }
}

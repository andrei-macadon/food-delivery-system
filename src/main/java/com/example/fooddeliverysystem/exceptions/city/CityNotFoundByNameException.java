package com.example.fooddeliverysystem.exceptions.city;

public class CityNotFoundByNameException extends RuntimeException{

    public CityNotFoundByNameException(String cityName) {
        super("The city " + cityName + " was not found.");
    }

}

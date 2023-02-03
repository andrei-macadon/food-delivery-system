package com.example.fooddeliverysystem.exceptions.foodcategory;

public class FoodCategoryAlreadyExistsException extends RuntimeException {

    public FoodCategoryAlreadyExistsException(String foodCategoryName) {
        super("Food category " + foodCategoryName + " already exists.");
    }
}

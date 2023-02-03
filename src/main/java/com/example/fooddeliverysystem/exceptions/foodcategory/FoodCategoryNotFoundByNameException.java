package com.example.fooddeliverysystem.exceptions.foodcategory;

public class FoodCategoryNotFoundByNameException extends RuntimeException {

    public FoodCategoryNotFoundByNameException(String foodCategoryName) {
        super("The food category with the name: " + foodCategoryName + " has not been found.");
    }
}

package com.example.fooddeliverysystem.exceptions.foodcategory;

public class FoodCategoryNotFoundByIdException extends RuntimeException {

    public FoodCategoryNotFoundByIdException(Long foodCategoryId) {
        super("The food category with the id " + foodCategoryId + " has not been found");
    }
}

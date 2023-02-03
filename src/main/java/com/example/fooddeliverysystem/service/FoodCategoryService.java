package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.FoodCategory;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface FoodCategoryService {
    List<FoodCategory> getAllFoodCategories();

    void addNewFoodCategory(FoodCategory foodCategory);

    FoodCategory searchFoodCategoryByName(String foodCategoryName);

    void addNewFoodCategoryToRestaurantMenu(FoodCategory foodCategory, Long restaurantId);

    void deleteFoodCategoryById(Long foodCategoryId);

    FoodCategory searchFoodCategoryById(Long foodCategoryId);

    void updateFoodCategory(Long foodCategoryId, FoodCategory foodCategory) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}

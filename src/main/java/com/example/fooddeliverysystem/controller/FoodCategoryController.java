package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.service.FoodCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/foodcategory")
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @Autowired
    public FoodCategoryController(FoodCategoryService foodCategoryService) {
        this.foodCategoryService = foodCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<FoodCategory>> getAllFoodCategories() {
        List<FoodCategory> foodCategories = foodCategoryService.getAllFoodCategories();
        return ResponseEntity.ok(foodCategories);
    }

    @GetMapping("/id/{food_category_id}")
    public ResponseEntity<FoodCategory> getFoodCategoryById(@PathVariable("food_category_id") Long foodCategoryId) {
        FoodCategory foodCategory = foodCategoryService.searchFoodCategoryById(foodCategoryId);
        return ResponseEntity.ok().body(foodCategory);
    }

    @GetMapping("/{name}")
    public ResponseEntity<FoodCategory> getFoodCategoryByName(@PathVariable("name") String foodCategoryName) {
        FoodCategory foodCategory = foodCategoryService.searchFoodCategoryByName(foodCategoryName);
        return ResponseEntity.ok().body(foodCategory);
    }

    @PostMapping
    public ResponseEntity<String> addFoodCategory(@RequestBody @Valid FoodCategory foodCategory) {
        foodCategoryService.addNewFoodCategory(foodCategory);
        return ResponseEntity.ok().body(foodCategory.getFoodCategoryName() + " has been added as a new food category.");
    }
    @PostMapping("/{restaurant_id}")
    public ResponseEntity<String> addFoodCategoryToRestaurantMenu(@RequestBody @Valid FoodCategory foodCategory,
                                                                  @PathVariable("restaurant_id") Long restaurantId) {
        foodCategoryService.addNewFoodCategoryToRestaurantMenu(foodCategory, restaurantId);
        return ResponseEntity.ok().body(foodCategory.getFoodCategoryName() + " has been added as a new food category" +
                " to the restaurant with the id " + restaurantId);
    }

    @DeleteMapping("/{food_category_id}")
    public ResponseEntity<String> deleteFoodCategoryById(@PathVariable("food_category_id") Long foodCategoryId) {
        foodCategoryService.deleteFoodCategoryById(foodCategoryId);
        return ResponseEntity.ok().body("The food category with the id " + foodCategoryId + " has been deleted.");
    }

    @PutMapping("/{food_category_id}")
    public ResponseEntity<String> updateFoodCategory(@PathVariable("food_category_id") Long foodCategoryId,
                                                     @RequestBody @Valid FoodCategory foodCategory)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        foodCategoryService.updateFoodCategory(foodCategoryId, foodCategory);
        return ResponseEntity.ok().body("The food category with the id: " + foodCategoryId + " was updated.");
    }
}

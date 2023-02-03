package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FoodCategoryRepo extends JpaRepository<FoodCategory, Long> {

    @Query("SELECT fc from FoodCategory fc WHERE fc.foodCategoryName = :name")
    Optional<FoodCategory> searchFoodCategoryByName(String name);
}

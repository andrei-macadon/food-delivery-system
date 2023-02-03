package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.FoodCategoryRepo;
import com.example.fooddeliverysystem.repo.RestaurantRepo;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class FoodCategoryServiceImpl implements FoodCategoryService{

    private final FoodCategoryRepo foodCategoryRepo;
    private final RestaurantRepo restaurantRepo;


    public FoodCategoryServiceImpl(FoodCategoryRepo foodCategoryRepo, RestaurantRepo restaurantRepo) {
        this.foodCategoryRepo = foodCategoryRepo;
        this.restaurantRepo = restaurantRepo;
    }

    @Override
    public List<FoodCategory> getAllFoodCategories() {
        return foodCategoryRepo.findAll();
    }

    @Override
    public void addNewFoodCategory(FoodCategory foodCategory) {
        Optional<FoodCategory> foodCategoryOptional
                = foodCategoryRepo.searchFoodCategoryByName(foodCategory.getFoodCategoryName());

        foodCategoryOptional.ifPresentOrElse(
                (x) -> {
                    throw new FoodCategoryAlreadyExistsException(x.getFoodCategoryName());
                },
                () -> foodCategoryRepo.save(foodCategory)
        );
    }

    @Override
    public FoodCategory searchFoodCategoryByName(String foodCategoryName) {

        var foodCategory = foodCategoryRepo.searchFoodCategoryByName(StringUtils.capitalize(foodCategoryName))
                .orElseThrow(() -> new FoodCategoryNotFoundByNameException(foodCategoryName));

        return foodCategory;
    }

    @Override
    public void addNewFoodCategoryToRestaurantMenu(FoodCategory foodCategory, Long restaurantId) {
        Optional<FoodCategory> optionalFoodCategory = foodCategoryRepo
                .searchFoodCategoryByName(foodCategory.getFoodCategoryName());

        optionalFoodCategory.ifPresentOrElse(
                (x) -> {
                    throw new FoodCategoryAlreadyExistsException(x.getFoodCategoryName());
                },
                () -> {
                    Restaurant restaurant = restaurantRepo.findById(restaurantId)
                            .orElseThrow(() -> new RestaurantNotFoundByIdException(restaurantId));

                    foodCategory.setRestaurantMenu(restaurant);
                    foodCategoryRepo.save(foodCategory);
                }
        );
    }

    @Override
    public void deleteFoodCategoryById(Long foodCategoryId) {
        foodCategoryRepo.deleteById(foodCategoryId);
    }

    @Override
    public FoodCategory searchFoodCategoryById(Long foodCategoryId) {
        return foodCategoryRepo.findById(foodCategoryId)
                .orElseThrow(() -> new FoodCategoryNotFoundByIdException(foodCategoryId));
    }


    public void copyNonNullAttr(FoodCategory toBeUpdatedFoodCategory, FoodCategory foodCategory)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        PropertyUtils.describe(foodCategory).entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> {
                    try {
                        PropertyUtils.setProperty(toBeUpdatedFoodCategory, entry.getKey(), entry.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException("Food Category sent via HTTP can not be mapped to FoodCategory from the db.");
                    }
                });
    }
    @Override
    @Transactional
    public void updateFoodCategory(Long foodCategoryId, FoodCategory foodCategory)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        FoodCategory toBeUpdatedFoodCategory = foodCategoryRepo.findById(foodCategoryId)
                .orElseThrow(() -> new FoodCategoryNotFoundByIdException(foodCategoryId));

        copyNonNullAttr(toBeUpdatedFoodCategory, foodCategory);

    }
}

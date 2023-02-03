package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {

    @Query("SELECT mi from MenuItem mi WHERE mi.foodCategory.foodCategoryId = :foodcategoryid")
    List<MenuItem> searchMenuItemsByFoodCategory(Long foodcategoryid);

    @Query("SELECT mi from MenuItem mi WHERE mi.name = :name")
    Optional<MenuItem> searchMenuItemByName(String name);
}

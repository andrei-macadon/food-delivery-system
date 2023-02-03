package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    boolean existsByName(String s);

    @Query("SELECT r from Restaurant r WHERE r.name = :restaurantName")
    Optional<Restaurant> searchRestaurantByName(String restaurantName);
}

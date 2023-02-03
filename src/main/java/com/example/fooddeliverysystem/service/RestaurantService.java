package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<Restaurant> getRestaurants();

    Restaurant getRestaurantByName(String restaurantName);

    void addNewRestaurantToCity(Restaurant restaurant, Long cityId);

    Restaurant getRestaurantById(Long restaurantId);

    void deleteRestaurantById(Long restaurantId);

    void addNewRestaurant(Restaurant restaurant);
}

package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants() {

        List<Restaurant> restaurantList = restaurantService.getRestaurants();
        return ResponseEntity.ok().body(restaurantList);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("name") String restaurantName) {
        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);
        return ResponseEntity.ok().body(restaurant);
    }

    @GetMapping("/id/{restaurant_id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("restaurant_id") Long restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok().body(restaurant);
    }


    @PostMapping
    public ResponseEntity<String> addNewRestaurant(@RequestBody @Valid Restaurant restaurant) {
        restaurantService.addNewRestaurant(restaurant);
        return ResponseEntity.ok().body("The restaurant " + restaurant.getName() + " has been added.");
    }
    @PostMapping("/{city_id}")
    public ResponseEntity<String> addNewRestaurantToCity(@RequestBody @Valid Restaurant restaurant,
                                                   @PathVariable("city_id") Long cityId) {
        restaurantService.addNewRestaurantToCity(restaurant, cityId);
        return ResponseEntity.ok().body("The restaurant " + restaurant.getName() + " has been added to the city " +
                "with the id " + cityId);

    }

    @DeleteMapping("/{restaurant_id}")
    public ResponseEntity<String> deleteRestaurantById(@PathVariable("restaurant_id") Long restaurantId) {
        restaurantService.deleteRestaurantById(restaurantId);
        return ResponseEntity.ok().body("The restaurant with the id " + restaurantId + " has been deleted.");
    }

}

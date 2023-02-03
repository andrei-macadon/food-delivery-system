package com.example.fooddeliverysystem.service;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByNameException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.repo.CityRepo;
import com.example.fooddeliverysystem.repo.RestaurantRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.capitalize;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepo restaurantRepo;

    private final CityRepo cityRepo;

    public RestaurantServiceImpl(RestaurantRepo restaurantRepo, CityRepo cityRepo) {
        this.restaurantRepo = restaurantRepo;
        this.cityRepo = cityRepo;
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return restaurantRepo.findAll();
    }

    @Override
    public Restaurant getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundByIdException(restaurantId));
        return restaurant;
    }

    @Override
    public void deleteRestaurantById(Long restaurantId) {
        restaurantRepo.deleteById(restaurantId);
    }

    @Override
    public void addNewRestaurant(Restaurant restaurant) {
        Optional<Restaurant> optionalRestaurant = restaurantRepo.searchRestaurantByName(restaurant.getName());

        optionalRestaurant.ifPresentOrElse(
                (x) -> {
                  throw new RestaurantAlreadyExistsException(x.getName());
                },
                () -> {
                    restaurantRepo.save(restaurant);
                }
        );
    }

    @Override
    public Restaurant getRestaurantByName(String restaurantName) {

        Restaurant restaurantResult = restaurantRepo.searchRestaurantByName(capitalize(restaurantName))
                .orElseThrow(() -> new RestaurantNotFoundByNameException(restaurantName));

        return restaurantResult;
    }

    @Override
    @Transactional
    public void addNewRestaurantToCity(Restaurant restaurant, Long cityId) {

        Optional<Restaurant> optionalRestaurant = restaurantRepo.searchRestaurantByName(restaurant.getName());

        optionalRestaurant.ifPresentOrElse(
                (x) -> {
                    throw new RestaurantAlreadyExistsException(x.getName());
                },
                () -> {
                    City city = cityRepo.findById(cityId)
                            .orElseThrow(() -> new CityNotFoundByIdException(cityId));

                    restaurant.setCity(city);
                    restaurantRepo.save(restaurant);
                }
        );

    }


}

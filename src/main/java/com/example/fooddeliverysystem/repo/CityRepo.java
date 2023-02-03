package com.example.fooddeliverysystem.repo;

import com.example.fooddeliverysystem.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CityRepo extends JpaRepository<City, Long> {

    @Query("SELECT c FROM City c WHERE c.name = :cityName")
    Optional<City> searchCityByName(String cityName);
}

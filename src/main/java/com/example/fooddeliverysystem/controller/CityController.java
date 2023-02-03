package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.model.Restaurant;
import com.example.fooddeliverysystem.service.CityService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping()
    public ResponseEntity<List<City>> getCities() {
        List<City> cities = cityService.getCities();
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping("/id/{id_no}")
    public ResponseEntity<City> getCityById(@PathVariable("id_no") Long cityId) {
        City city = cityService.getCityById(cityId);
        System.out.println(city.getRestaurants());
        return ResponseEntity.ok().body(city);

    }

    @GetMapping("/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable("name") String cityName) {
        City city = cityService.getCityByName(cityName);
        return ResponseEntity.ok().body(city);
    }

    @PostMapping
    public ResponseEntity<String> addNewCity(@RequestBody City city) {
        cityService.addNewCity(city);
        return ResponseEntity.ok().body("The city " + city.getName() + " has been added.");
    }

    @DeleteMapping("/{city_id}")
    public ResponseEntity<String> deleteCityById(@PathVariable("city_id") Long cityId) {

        cityService.deleteCityById(cityId);
        return ResponseEntity.ok().body("The city with the id " + cityId + " has been deleted.");
    }
}

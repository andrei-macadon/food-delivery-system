package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.City;

import java.util.List;

public interface CityService {

    List<City> getCities();

    City getCityByName(String cityName);

    City getCityById(Long cityId);

    void addNewCity(City city);

    void deleteCityById(Long cityId);
}

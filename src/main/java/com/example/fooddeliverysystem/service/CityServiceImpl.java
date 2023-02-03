package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.exceptions.city.CityAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByNameException;
import com.example.fooddeliverysystem.model.City;
import com.example.fooddeliverysystem.repo.CityRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepo cityRepo;

    public CityServiceImpl(CityRepo cityRepo) {
        this.cityRepo = cityRepo;
    }


    @Override
    public List<City> getCities() {
        return cityRepo.findAll();
    }

    @Override
    public City getCityByName(String cityName) {
        String capitalizedCityName = StringUtils.capitalize(cityName);
        City city = cityRepo.searchCityByName(capitalizedCityName)
                .orElseThrow(() -> new CityNotFoundByNameException(capitalizedCityName));
        return city;
    }

    @Override
    public City getCityById(Long cityId) {
        City city = cityRepo.findById(cityId)
                .orElseThrow(() -> new CityNotFoundByIdException(cityId));
        return city;
    }

    @Override
    public void addNewCity(City city) {
        Optional<City> city1 = cityRepo.searchCityByName(city.getName());
        city1.ifPresentOrElse(x -> {
            throw new CityAlreadyExistsException(x.getName());
            },
            () -> cityRepo.save(city)
        );


    }

    @Override
    public void deleteCityById(Long cityId) {
        cityRepo.deleteById(cityId);
    }
}

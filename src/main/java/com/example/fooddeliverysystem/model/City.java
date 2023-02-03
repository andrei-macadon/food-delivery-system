package com.example.fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @NotBlank(message = "The name of the city should not be blank.")
    private String name;
    @NotBlank(message = "The zipcode of the city should not be blank")
    private String zipcode;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Restaurant> restaurants;

    public City() {
    }

    public City(Long cityId, String name, String zipcode, List<Restaurant> restaurants) {
        this.cityId = cityId;
        this.name = name;
        this.zipcode = zipcode;
        this.restaurants = restaurants;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}

package com.example.fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @NotBlank(message = "The name of the restaurant should not be blank")
    private String name;
    @NotBlank(message = "The address of the restaurant should not be blank")
    @Size(min = 5, message = "The address of the restaurant should be at least 5 characters long")
    private String address;

    @OneToMany(mappedBy = "restaurantMenu", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<FoodCategory> foodCategoryList;

    @ManyToOne
    @JoinColumn(name = "city_id",
            referencedColumnName = "cityId")
    private City city;

    public Restaurant() {
    }

    public Restaurant(Long restaurantId, String name, String address, List<FoodCategory> foodCategoryList, City city) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;
        this.foodCategoryList = foodCategoryList;
        this.city = city;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FoodCategory> getFoodCategoryList() {
        return foodCategoryList;
    }

    public void setFoodCategoryList(List<FoodCategory> foodCategoryList) {
        this.foodCategoryList = foodCategoryList;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", foodCategoryList=" + foodCategoryList +
                ", city=" + city +
                '}';
    }
}

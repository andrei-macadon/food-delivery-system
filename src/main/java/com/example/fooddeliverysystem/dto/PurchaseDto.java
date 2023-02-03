package com.example.fooddeliverysystem.dto;

import com.example.fooddeliverysystem.model.Customer;
import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.model.Restaurant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public class PurchaseDto {

    @NotNull(message = "The restaurant can not be null.")
    private Restaurant restaurant;

    @NotNull(message = "The customer can not be null.")
    private Customer customer;

    @NotBlank(message = "The purchase's time when it was placed can not be blank.")
    private String purchasePlacedTime;

    private String actualDeliveryTime;

    @NotNull(message = "The menu items field can not be null.")
    private List<MenuItem> menuItems;

    public PurchaseDto() {
    }

    public PurchaseDto(Restaurant restaurant, Customer customer, String purchasePlacedTime,
                       String actualDeliveryTime, List<MenuItem> menuItems) {
        this.restaurant = restaurant;
        this.customer = customer;
        this.purchasePlacedTime = purchasePlacedTime;
        this.actualDeliveryTime = actualDeliveryTime;
        this.menuItems = menuItems;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPurchasePlacedTime() {
        return purchasePlacedTime;
    }

    public void setPurchasePlacedTime(String purchasePlacedTime) {
        this.purchasePlacedTime = purchasePlacedTime;
    }

    public String getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(String actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

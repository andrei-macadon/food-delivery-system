package com.example.fooddeliverysystem.model;

import com.example.fooddeliverysystem.exceptions.NoItemsWereSelectedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Entity
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id",
            referencedColumnName = "restaurantId")
    @NotNull(message = "The restaurant can not be null.")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "customer_id",
            referencedColumnName = "customer_id")
    @NotNull(message = "The customer can not be null.")
    private Customer customer;

    @NotNull(message = "The purchase's time when it was placed can not be null.")
    private LocalDateTime purchasePlacedTime;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;

    @ManyToMany
    @JoinTable(name = "PURCHASE_MENUITEMS",
            joinColumns = @JoinColumn(name = "purchaseId"),
            inverseJoinColumns = @JoinColumn(name = "menuItemId")
    )
    @NotNull(message = "The menu items field can not be null.")
    private List<MenuItem> menuItems;

    public Purchase() {
    }

    public Purchase(Long purchaseId, BigDecimal price, Restaurant restaurant, Customer customer,
                    LocalDateTime purchasePlacedTime, LocalDateTime estimatedDeliveryTime,
                    LocalDateTime actualDeliveryTime, List<MenuItem> menuItems) {
        this.purchaseId = purchaseId;
        this.price = price;
        this.restaurant = restaurant;
        this.customer = customer;
        this.purchasePlacedTime = purchasePlacedTime;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.actualDeliveryTime = actualDeliveryTime;
        this.menuItems = menuItems;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public LocalDateTime getPurchasePlacedTime() {
        return purchasePlacedTime;
    }

    public void setPurchasePlacedTime(LocalDateTime purchasePlacedTime) {
        this.purchasePlacedTime = purchasePlacedTime;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}

package com.example.fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Builder
public class FoodCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodCategoryId;

    @NotBlank(message = "The name of the food category should not be blank.")
    private String foodCategoryName;

    @ManyToOne
    @JoinColumn(
            name = "restaurant_id",
            referencedColumnName = "restaurantId"
    )
    private Restaurant restaurantMenu;


    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<MenuItem> menuItems;

    public FoodCategory() {
    }

    public FoodCategory(Long foodCategoryId, String foodCategoryName, Restaurant restaurantMenu, List<MenuItem> menuItems) {
        this.foodCategoryId = foodCategoryId;
        this.foodCategoryName = foodCategoryName;
        this.restaurantMenu = restaurantMenu;
        this.menuItems = menuItems;
    }

    public Long getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(Long foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public String getFoodCategoryName() {
        return foodCategoryName;
    }

    public void setFoodCategoryName(String foodCategoryName) {
        this.foodCategoryName = foodCategoryName;
    }

    public Restaurant getRestaurantMenu() {
        return restaurantMenu;
    }

    public void setRestaurantMenu(Restaurant restaurantMenu) {
        this.restaurantMenu = restaurantMenu;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

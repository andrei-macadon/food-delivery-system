package com.example.fooddeliverysystem.model;


import lombok.Builder;
import org.springframework.context.annotation.EnableMBeanExport;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuItemId;

    @ManyToOne
    @JoinColumn(name = "category_id",
            referencedColumnName = "foodCategoryId")
    private FoodCategory foodCategory;
    @NotBlank(message = "The name of the menu item cannot be null.")
    private String name;
    @Lob
    @Column
    @NotBlank(message = "The ingredients of the menu item cannot be null.")
    private String ingredients;

    @NotNull(message = "The price of the menu item cannot be null.")
    @Min(value = 1, message = "The price of the menu item must be bigger than 0.")
    private BigDecimal price;

    @NotNull(message = "The timeToCook of the menu item cannot be null.")
    @Min(value = 10, message = "TimeToCook must be at least 10 minutes.")
    private BigDecimal timeToCook; // in minutes

    public MenuItem() {
    }

    public MenuItem(Long menuItemId, FoodCategory foodCategory, String name, String ingredients, BigDecimal price, BigDecimal timeToCook) {
        this.menuItemId = menuItemId;
        this.foodCategory = foodCategory;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.timeToCook = timeToCook;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public FoodCategory getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(FoodCategory foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(BigDecimal timeToCook) {
        this.timeToCook = timeToCook;
    }
}

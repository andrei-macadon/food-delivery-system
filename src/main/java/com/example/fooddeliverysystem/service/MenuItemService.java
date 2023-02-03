package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.model.MenuItem;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> searchMenuItemsByFoodCategory(Long foodcategoryid);

    void addNewMenuItem(MenuItem menuItem);

    MenuItem getMenuItemById(Long menuitemId);

    List<MenuItem> getAllMenuItems();

    void addNewMenuItemToFoodCategory(MenuItem menuItem, Long foodCategoryId);

    void deleteMenuItem(Long menuitemId);

    void updateMenuItem(Long menuitemId, MenuItem menuItem) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}

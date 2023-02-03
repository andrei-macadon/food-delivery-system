package com.example.fooddeliverysystem.service;


import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemNotFoundByIdException;
import com.example.fooddeliverysystem.model.FoodCategory;
import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.repo.FoodCategoryRepo;
import com.example.fooddeliverysystem.repo.MenuItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.beanutils.PropertyUtils;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepo menuItemRepo;
    private final FoodCategoryRepo foodCategoryRepo;

    public MenuItemServiceImpl(MenuItemRepo menuItemRepo, FoodCategoryRepo foodCategoryRepo) {
        this.menuItemRepo = menuItemRepo;
        this.foodCategoryRepo = foodCategoryRepo;
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> searchMenuItemsByFoodCategory(Long foodcategoryid) {

        foodCategoryRepo.findById(foodcategoryid)
                .orElseThrow(() -> new FoodCategoryNotFoundByIdException(foodcategoryid));

        List<MenuItem> menuItems = menuItemRepo.searchMenuItemsByFoodCategory(foodcategoryid);
        return menuItems;
    }

    @Override
    public MenuItem getMenuItemById(Long menuitemId) {
        return menuItemRepo.findById(menuitemId)
                .orElseThrow(() -> new MenuItemNotFoundByIdException(menuitemId));
    }

    @Override
    public void addNewMenuItem(MenuItem menuItem) {
        Optional<MenuItem> optionalMenuItem = menuItemRepo.searchMenuItemByName(menuItem.getName());

        optionalMenuItem.ifPresentOrElse(
                (x) -> {
                    throw new MenuItemAlreadyExistsException(x.getName());
                },
                () -> menuItemRepo.save(menuItem)
        );
    }

    @Override
    @Transactional
    public void addNewMenuItemToFoodCategory(MenuItem menuItem, Long foodCategoryId) {

        Optional<MenuItem> optionalMenuItem = menuItemRepo.searchMenuItemByName(menuItem.getName());

        optionalMenuItem.ifPresentOrElse(
                (x) -> {
                    throw new MenuItemAlreadyExistsException(x.getName());
                },
                () -> {
                    FoodCategory foodCategory = foodCategoryRepo.findById(foodCategoryId)
                            .orElseThrow(() -> new FoodCategoryNotFoundByIdException(foodCategoryId));

                    menuItem.setFoodCategory(foodCategory);
                    menuItemRepo.save(menuItem);
                }
        );
    }

    @Override
    public void deleteMenuItem(Long menuitemId) {
        menuItemRepo.deleteById(menuitemId);
    }

    public void copyNonNullAttr(MenuItem toBeUpdatedMenuItem, MenuItem menuItem)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        PropertyUtils.describe(menuItem).entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .forEach(entry -> {
                    try {
                        PropertyUtils.setProperty(toBeUpdatedMenuItem, entry.getKey(), entry.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException("Menu Item sent via HTTP can not be mapped to MenuItem from the db.");
                    }
                });
    }

    @Override
    @Transactional
    public void updateMenuItem(Long menuitemId, MenuItem menuItem)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        MenuItem toBeUpdatedMenuItem = menuItemRepo.findById(menuitemId)
                .orElseThrow(() -> new MenuItemNotFoundByIdException(menuitemId));

        this.copyNonNullAttr(toBeUpdatedMenuItem, menuItem);
    }

}

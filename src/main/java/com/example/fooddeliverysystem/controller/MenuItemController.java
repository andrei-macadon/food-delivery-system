package com.example.fooddeliverysystem.controller;

import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.service.MenuItemService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menuitem")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }


    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{foodcategoryid}")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsFromCategory(@PathVariable("foodcategoryid") Long foodcategoryId) {
        List<MenuItem> menuItems = menuItemService.searchMenuItemsByFoodCategory(foodcategoryId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/id/{menuitem_id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable("menuitem_id") Long menuitemId) {
        MenuItem menuItem = menuItemService.getMenuItemById(menuitemId);
        return ResponseEntity.ok().body(menuItem);
    }

    @PostMapping
    public ResponseEntity<String> addNewMenuItem(@RequestBody @Valid MenuItem menuItem) {
        menuItemService.addNewMenuItem(menuItem);
        return ResponseEntity.ok().body(menuItem.getName() + " has been added to the "
                + menuItem.getFoodCategory().getFoodCategoryName() + " food category.");
    }

    @PostMapping("/{food_category_id}")
    public ResponseEntity<String> addNewMenuItemToFoodCategory(@RequestBody @Valid MenuItem menuItem,
                                                               @PathVariable("food_category_id") Long foodCategoryId) {

        menuItemService.addNewMenuItemToFoodCategory(menuItem, foodCategoryId);
        return ResponseEntity.ok().body(menuItem.getName() + " has been added to the "
                + menuItem.getFoodCategory().getFoodCategoryName() + " food category.");
    }

    @DeleteMapping("/{menuitem_id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable("menuitem_id") Long menuitemId) {

        menuItemService.deleteMenuItem(menuitemId);
        return ResponseEntity.ok().body("The menu item with the id: " + menuitemId + " has been deleted.");
    }

    @PutMapping("/{menuitem_id}")
    public ResponseEntity<String> updateMenuItem(@PathVariable("menuitem_id") Long menuitemId,
                                                 @RequestBody @Valid MenuItem menuItem)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        menuItemService.updateMenuItem(menuitemId, menuItem);
        return ResponseEntity.ok().body("The menu item with the id: " + menuitemId + " has been updated.");
    }
}

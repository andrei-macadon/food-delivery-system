package com.example.fooddeliverysystem.exceptions.menuitem;

public class MenuItemAlreadyExistsException extends RuntimeException {

    public MenuItemAlreadyExistsException(String menuItemName) {
        super("Menu item " + menuItemName + " already exists.");
    }
}

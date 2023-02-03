package com.example.fooddeliverysystem.exceptions.menuitem;

public class MenuItemNotFoundByIdException extends RuntimeException {

    public MenuItemNotFoundByIdException(Long menuitemId) {
        super("The menu item with the id: " + menuitemId + " has not been found.");
    }
}

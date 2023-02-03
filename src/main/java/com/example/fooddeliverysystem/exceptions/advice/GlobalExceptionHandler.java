package com.example.fooddeliverysystem.exceptions.advice;

import com.example.fooddeliverysystem.exceptions.city.CityAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.city.CityNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.customer.CustomerNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.foodcategory.FoodCategoryNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.menuitem.MenuItemNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.purchase.IncorrectDateFormatException;
import com.example.fooddeliverysystem.exceptions.purchase.PurchaseNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantAlreadyExistsException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByIdException;
import com.example.fooddeliverysystem.exceptions.restaurant.RestaurantNotFoundByNameException;
import com.example.fooddeliverysystem.exceptions.role.RoleAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // CITY Exception Handlers
    @ExceptionHandler({CityNotFoundByNameException.class})
    public ResponseEntity<String> handle(CityNotFoundByNameException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({CityNotFoundByIdException.class})
    public ResponseEntity<String> handle(CityNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({CityAlreadyExistsException.class})
    public ResponseEntity<String> handle(CityAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }
    // RESTAURANT Exception Handlers
    @ExceptionHandler({RestaurantNotFoundByNameException.class})
    public ResponseEntity<String> handle(RestaurantNotFoundByNameException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({RestaurantNotFoundByIdException.class})
    public ResponseEntity<String> handle(RestaurantNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({RestaurantAlreadyExistsException.class})
    public ResponseEntity<String> handle(RestaurantAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // FOOD CATEGORY Exception Handlers
    @ExceptionHandler({FoodCategoryNotFoundByIdException.class})
    public ResponseEntity<String> handle(FoodCategoryNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }
    @ExceptionHandler({FoodCategoryNotFoundByNameException.class})
    public ResponseEntity<String> handle(FoodCategoryNotFoundByNameException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({FoodCategoryAlreadyExistsException.class})
    public ResponseEntity<String> handle(FoodCategoryAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // MENU ITEM Exception Handlers
    @ExceptionHandler({MenuItemNotFoundByIdException.class})
    public ResponseEntity<String> handle(MenuItemNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({MenuItemAlreadyExistsException.class})
    public ResponseEntity<String> handle(MenuItemAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // PURCHASE Exception Handlers
    @ExceptionHandler({PurchaseNotFoundByIdException.class})
    public ResponseEntity<String> handle(PurchaseNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({IncorrectDateFormatException.class})
    public ResponseEntity<String> handle(IncorrectDateFormatException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // CUSTOMER Exception Handlers
    @ExceptionHandler({CustomerNotFoundByNameException.class})
    public ResponseEntity<String> handle(CustomerNotFoundByNameException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({CustomerNotFoundByIdException.class})
    public ResponseEntity<String> handle(CustomerNotFoundByIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    @ExceptionHandler({CustomerAlreadyExistsException.class})
    public ResponseEntity<String> handle(CustomerAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

    // ROLE Exception Handlers
    @ExceptionHandler({RoleAlreadyExistsException.class})
    public ResponseEntity<String> handle(RoleAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(e.getMessage() + " at " + LocalDateTime.now());
    }

}

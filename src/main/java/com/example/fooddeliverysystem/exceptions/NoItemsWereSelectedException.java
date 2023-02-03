package com.example.fooddeliverysystem.exceptions;

public class NoItemsWereSelectedException extends RuntimeException {

    public NoItemsWereSelectedException() {
        super("No items were selected in order to compute the longest time to cook one.");
    }
}

package com.example.fooddeliverysystem.exceptions.purchase;

public class PurchaseNotFoundByIdException extends RuntimeException {

    public PurchaseNotFoundByIdException(Long purchaseId) {
        super("The purchase with the id: " + purchaseId + " could not have been found.");
    }
}

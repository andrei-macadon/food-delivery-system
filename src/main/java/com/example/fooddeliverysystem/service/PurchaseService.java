package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.dto.PurchaseDto;
import com.example.fooddeliverysystem.model.Purchase;

import java.util.List;

public interface PurchaseService {
    void addPurchase(PurchaseDto purchaseDto);

    List<Purchase> getAllPurchases();

    Purchase getPurchaseById(Long purchaseId);

    void updateActualDeliveryTime(Long purchaseId, String actualDeliveryTime);
}

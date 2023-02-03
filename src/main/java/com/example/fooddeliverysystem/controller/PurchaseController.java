package com.example.fooddeliverysystem.controller;


import com.example.fooddeliverysystem.dto.PurchaseDto;
import com.example.fooddeliverysystem.model.Purchase;
import com.example.fooddeliverysystem.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok().body(purchases);
    }

    @GetMapping("/id/{purchase_id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable("purchase_id") Long purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        return ResponseEntity.ok().body(purchase);
    }

    @PostMapping
    public ResponseEntity<String> placeNewPurchase(@RequestBody @Valid PurchaseDto purchaseDto) {
        purchaseService.addPurchase(purchaseDto);
        return ResponseEntity.ok().body("The purchase has been successfully added to the database.");
    }

    @PutMapping("/{purchase_id}")
    public ResponseEntity<String> updateActualDeliveryTime(@PathVariable("purchase_id") Long purchaseId,
                                                           @RequestBody String actualDeliveryTime) {
        purchaseService.updateActualDeliveryTime(purchaseId, actualDeliveryTime);
        return ResponseEntity.ok()
                .body("The actual delivery time got updated to " + actualDeliveryTime);
    }

}

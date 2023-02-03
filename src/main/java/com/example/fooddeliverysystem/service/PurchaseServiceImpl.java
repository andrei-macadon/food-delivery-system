package com.example.fooddeliverysystem.service;

import com.example.fooddeliverysystem.dto.PurchaseDto;
import com.example.fooddeliverysystem.exceptions.NoItemsWereSelectedException;
import com.example.fooddeliverysystem.exceptions.purchase.IncorrectDateFormatException;
import com.example.fooddeliverysystem.exceptions.purchase.PurchaseNotFoundByIdException;
import com.example.fooddeliverysystem.model.MenuItem;
import com.example.fooddeliverysystem.model.Purchase;
import com.example.fooddeliverysystem.repo.PurchaseRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;


@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepo purchaseRepo;
    private final DateTimeFormatter dateTimeFormatter;

    public PurchaseServiceImpl(PurchaseRepo purchaseRepo, DateTimeFormatter dateTimeFormatter) {
        this.purchaseRepo = purchaseRepo;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    private BigDecimal computeTimeToCook(Purchase purchase) {
        MenuItem longestToCook = purchase.getMenuItems().stream()
                .max(Comparator.comparing(MenuItem::getTimeToCook))
                .orElseThrow(NoItemsWereSelectedException::new);
        return longestToCook.getTimeToCook();
    }

    private void computeEstimatedDeliveryTime(Purchase purchase) {
        // 30 minutes is the transportation time
        purchase.setEstimatedDeliveryTime(
                purchase.getPurchasePlacedTime().plusMinutes(30 + computeTimeToCook(purchase).intValue())
        );
//        return purchase;
    }
    @Override
    public void addPurchase(PurchaseDto purchaseDto) {

        Purchase purchase = new Purchase();
        purchase.setRestaurant(purchaseDto.getRestaurant());
        purchase.setCustomer(purchaseDto.getCustomer());
        purchase.setMenuItems(purchaseDto.getMenuItems());

        //handling the purchasePlacedTime DATE
        LocalDateTime realPurchasePlacedTime = null;
        try {
            realPurchasePlacedTime = LocalDateTime
                    .parse(purchaseDto.getPurchasePlacedTime(), dateTimeFormatter);
        } catch (IncorrectDateFormatException e) {
            throw new IncorrectDateFormatException(
                    "The date should be in the format: yyyy-MM-dd HH:mm",
                    purchaseDto.getPurchasePlacedTime(),
                    0
                    );
        }
        purchase.setPurchasePlacedTime(realPurchasePlacedTime);


        // setting the price of the order
        Integer totalPrice = purchase.getMenuItems().stream()
                .map((mi) -> mi.getPrice().intValue())
                .reduce(0, Integer::sum);

        purchase.setPrice(BigDecimal.valueOf(totalPrice));

        // setting the estimated arrival time
        computeEstimatedDeliveryTime(purchase);

        purchaseRepo.save(purchase);
    }

    @Override
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = purchaseRepo.findAll();
        return purchases;
    }

    @Override
    public Purchase getPurchaseById(Long purchaseId) {
        return purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundByIdException(purchaseId));
    }

    @Override
    @Transactional
    public void updateActualDeliveryTime(Long purchaseId, String actualDeliveryTime) {
        Purchase purchase = purchaseRepo.findById(purchaseId)
                .orElseThrow(() -> new PurchaseNotFoundByIdException(purchaseId));
        try {
            purchase.setActualDeliveryTime(LocalDateTime.parse(actualDeliveryTime, dateTimeFormatter));
        } catch(IncorrectDateFormatException e){
            throw new IncorrectDateFormatException(
                    "The date should be in the format: yyyy-MM-dd HH:mm",
                    actualDeliveryTime,
                    0
            );
        }
    }
}

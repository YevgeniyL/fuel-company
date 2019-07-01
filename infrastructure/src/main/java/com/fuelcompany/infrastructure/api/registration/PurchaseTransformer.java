package com.fuelcompany.infrastructure.api.registration;

import com.fuelcompany.domain.aggregateModels.purchase.Purchase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseTransformer {
    public Purchase toDomain(ApiPurchase request) {
        return new Purchase(
                request.getFuelType(),
                request.getVolume(),
                request.getPrice(),
                request.getDriverId(),
                request.getDate()
        );
    }

    public ApiPurchase toREST(Purchase savedPurchase) {
        return new ApiPurchase(
                savedPurchase.getId(),
                savedPurchase.getVolume().stripTrailingZeros(),
                savedPurchase.getFuelType(),
                savedPurchase.getPrice().stripTrailingZeros(),
                savedPurchase.getDriverId(),
                savedPurchase.getDate());
    }

    public List<Purchase> toDomain(List<ApiPurchase> apiPurchaseList) {
        return apiPurchaseList.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
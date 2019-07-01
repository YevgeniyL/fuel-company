package com.fuelcompany.domain.service;

import com.fuelcompany.domain.aggregateModels.purchase.Purchase;
import com.fuelcompany.domain.errors.DomainException;

import java.util.List;

public interface PurchaseService {

    Purchase save(Purchase purchase) throws DomainException;

    List<Purchase> save(List<Purchase> purchaseList) throws DomainException;
}

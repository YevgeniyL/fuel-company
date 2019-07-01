package com.fuelcompany.domain.aggregateModels.purchase;

import com.fuelcompany.domain.aggregateModels.purchase.entity.FuelTypeEntity;
import com.fuelcompany.domain.aggregateModels.purchase.entity.PurchaseEntity;
import com.fuelcompany.domain.errors.DomainException;
import com.fuelcompany.domain.errors.ErrorMessages;
import com.fuelcompany.domain.repository.FuelTypeRepository;
import com.fuelcompany.domain.repository.PurchaseRepository;
import com.fuelcompany.domain.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Aggregate root element - Save incoming PurchaseItem records
 */

@Service
public class AggregatePurchase implements PurchaseService {
    private static Logger logger = LoggerFactory.getLogger(AggregatePurchase.class);

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private FuelTypeRepository fuelTypeRepository;
    @Value("${application.registration.purchase.file.maxRecordsCount:10000}")
    private Long maxRecordsInFile;

    @Override
    public Purchase save(Purchase purchase) throws DomainException {
        try {
            validateEmptyFields(purchase);
            validateNotPositiveFields(purchase);
            FuelTypeEntity fuelType = validateToExistFuelTypesFromRequest(purchase);
            Purchase result = buildDomainModel(saveNewPurchase(fuelType, purchase));
            logger.info("1 purchase saved. Id=" + result.getId());
            return result;
        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Saving process error", e);
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_E9999);
        }
    }

    @Override
    public List<Purchase> save(List<Purchase> purchaseList) throws DomainException {
        try {
            if (purchaseList.isEmpty())
                throw new DomainException(ErrorMessages.DOMAIN_ERROR_E1050);
            if (purchaseList.size() > maxRecordsInFile)
                throw new DomainException(ErrorMessages.DOMAIN_ERROR_E1051);

            purchaseList.forEach(purchase -> {
                validateEmptyFields(purchase);
                validateNotPositiveFields(purchase);
            });

            Set<String> fuelTypeNameList = purchaseList.stream().map(Purchase::getFuelType).collect(Collectors.toSet());
            Map<String, FuelTypeEntity> existNameMap = validateToExistFuelTypesFromRequest(fuelTypeNameList);

            List<Purchase> collect = purchaseList.stream()
                    .map(purchase -> buildDomainModel(saveNewPurchase(existNameMap.get(purchase.getFuelType()), purchase)))
                    .collect(Collectors.toList());
            logger.info("Saved " + collect.size() + " records from file.");
            return collect;

        } catch (DomainException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Saving process error", e);
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_E9999);
        }
    }

    private PurchaseEntity saveNewPurchase(FuelTypeEntity fuelTypeEntity, Purchase purchase) {
        return purchaseRepository.save(buildEntity(fuelTypeEntity, purchase));
    }

    private PurchaseEntity buildEntity(FuelTypeEntity fuelTypeEntity, Purchase item) {
        return new PurchaseEntity(fuelTypeEntity, item.getVolume(), item.getPrice(), item.getDriverId(), item.getDate());
    }

    private void validateEmptyFields(Purchase purchase) {
        if (purchase.getDate() == null)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1001);
        if (purchase.getPrice() == null)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1003);
        if (purchase.getDriverId() == null)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1004);
        if (purchase.getVolume() == null)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1006);
        if (StringUtils.isEmpty(purchase.getFuelType()))
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1002);
    }

    private void validateNotPositiveFields(Purchase purchase) {
        if (purchase.getVolume().compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1007);
        if (purchase.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1008);
        if (purchase.getDriverId() < 0)
            throw new DomainException(ErrorMessages.DOMAIN_ERROR_1009);
    }

    private FuelTypeEntity validateToExistFuelTypesFromRequest(Purchase purchase) {
        return fuelTypeRepository.findActiveByName(purchase.getFuelType())
                .orElseThrow(() -> new DomainException(ErrorMessages.DOMAIN_ERROR_1005));
    }

    private Map<String, FuelTypeEntity> validateToExistFuelTypesFromRequest(Set<String> requestNames) {
        Map<String, FuelTypeEntity> existNameMap = fuelTypeRepository.findActiveByName(requestNames)
                .stream()
                .collect(Collectors.toMap(FuelTypeEntity::getName, fuelTypeEntity -> fuelTypeEntity));

        boolean isAllMatched = requestNames.stream().allMatch(requestName -> existNameMap.containsKey(requestName));
        if (isAllMatched) return existNameMap;
        throw new DomainException(ErrorMessages.DOMAIN_ERROR_1005);
    }

    private Purchase buildDomainModel(PurchaseEntity entity) {
        return new Purchase(entity.getId(), entity.getVolume(), entity.getFuelType().getName(), entity.getPrice(), entity.getDriverId(), entity.getDate());
    }
}
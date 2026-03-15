package com.quantitymeasurement.controller;

import java.util.List;

import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.model.QuantityMeasurementEntity;
import com.quantitymeasurement.service.IQuantityMeasurementService;

public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    /**
     * Performs equality comparison between two quantities.
     */
    public boolean performComparison(QuantityDTO first, QuantityDTO second) {

        QuantityDTO result = service.compare(first, second);

        return result.getValue() == 1.0;
    }

    /**
     * Performs unit conversion.
     */
    public QuantityDTO performConversion(QuantityDTO source, QuantityDTO target) {

        return service.convert(source, target);
    }

    /**
     * Performs addition operation.
     */
    public QuantityDTO performAddition(
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO targetUnit) {

        return service.add(first, second, targetUnit);
    }

    /**
     * Performs subtraction operation.
     */
    public QuantityDTO performSubtraction(
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO targetUnit) {

        return service.subtract(first, second, targetUnit);
    }

    /**
     * Performs division operation.
     */
    public double performDivision(QuantityDTO first, QuantityDTO second) {

        QuantityDTO result = service.divide(first, second);
        return result.getValue();
    }

    /**
     * Returns operation history.
     */
    public List<QuantityMeasurementEntity> getHistory() {

        return service.getHistory();
    }
}
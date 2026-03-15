package com.quantitymeasurement.repository;

import java.util.List;

import com.quantitymeasurement.model.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

    /**
     * Saves a quantity measurement operation entity.
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Returns all stored measurement history.
     */
    List<QuantityMeasurementEntity> findAll();

    /**
     * Clears repository history.
     */
    void clear();
}
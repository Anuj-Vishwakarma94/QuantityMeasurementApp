package com.quantitymeasurement.service;

import java.util.List;

import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.model.QuantityMeasurementEntity;

public interface IQuantityMeasurementService {

    QuantityDTO compare(QuantityDTO first, QuantityDTO second);

    QuantityDTO convert(QuantityDTO source, QuantityDTO target);

    QuantityDTO add(QuantityDTO first, QuantityDTO second, QuantityDTO target);

    QuantityDTO subtract(QuantityDTO first, QuantityDTO second, QuantityDTO target);

    QuantityDTO divide(QuantityDTO first, QuantityDTO second);

    List<QuantityMeasurementEntity> getHistory();
}
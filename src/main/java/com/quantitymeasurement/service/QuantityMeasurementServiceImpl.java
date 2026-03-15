package com.quantitymeasurement.service;

import java.util.List;

import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.interfaces.SupportsArithmetic;
import com.quantitymeasurement.model.QuantityMeasurementEntity;
import com.quantitymeasurement.model.QuantityModel;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.TemperatureUnit;
import com.quantitymeasurement.unit.VolumeUnit;
import com.quantitymeasurement.unit.WeightUnit;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }

        this.repository = repository;
    }

    // ---------------- COMPARE ----------------

    @Override
    public QuantityDTO compare(QuantityDTO first, QuantityDTO second) {

        validateNotNull(first, second);
        validateSameMeasurementType(first, second);

        try {

            QuantityModel<IMeasurable> q1 = toModel(first);
            QuantityModel<IMeasurable> q2 = toModel(second);

            double base1 = toBaseValue(q1);
            double base2 = toBaseValue(q2);

            boolean result = Math.abs(base1 - base2) < 0.0001;

            repository.save(
                    new QuantityMeasurementEntity(
                            "COMPARE",
                            first.toString(),
                            second.toString(),
                            String.valueOf(result)));

            return new QuantityDTO(result ? 1.0 : 0.0, first.getUnit());

        } catch (Exception e) {
            throw saveAndWrap("COMPARE", first, second, e);
        }
    }

    // ---------------- CONVERT ----------------

    @Override
    public QuantityDTO convert(QuantityDTO source, QuantityDTO target) {

        validateNotNull(source, target);
        validateSameMeasurementType(source, target);

        try {

            QuantityModel<IMeasurable> sourceModel = toModel(source);
            QuantityModel<IMeasurable> targetModel = toModel(target);

            double base = toBaseValue(sourceModel);
            double converted = targetModel.getUnit().fromBase(base);

            QuantityDTO result =
                    new QuantityDTO(converted, mapApplicationUnitToDto(targetModel.getUnit()));

            repository.save(
                    new QuantityMeasurementEntity(
                            "CONVERT",
                            source.toString(),
                            result.toString()));

            return result;

        } catch (Exception e) {
            throw saveAndWrap("CONVERT", source, target, e);
        }
    }

    // ---------------- ADD ----------------

    @Override
    public QuantityDTO add(
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO target) {

        validateNotNull(first, second);
        validateSameMeasurementType(first, second);

        try {

            QuantityModel<IMeasurable> q1 = toModel(first);
            QuantityModel<IMeasurable> q2 = toModel(second);

            validateArithmeticSupported(q1.getUnit());

            double base1 = toBaseValue(q1);
            double base2 = toBaseValue(q2);

            double resultBase = base1 + base2;

            IMeasurable resultUnit = resolveResultUnit(q1, target, first);
            double resultValue = resultUnit.fromBase(resultBase);

            QuantityDTO result =
                    new QuantityDTO(resultValue, mapApplicationUnitToDto(resultUnit));

            repository.save(
                    new QuantityMeasurementEntity(
                            "ADD",
                            first.toString(),
                            second.toString(),
                            result.toString()));

            return result;

        } catch (Exception e) {
            throw saveAndWrap("ADD", first, second, e);
        }
    }

    // ---------------- SUBTRACT ----------------

    @Override
    public QuantityDTO subtract(
            QuantityDTO first,
            QuantityDTO second,
            QuantityDTO target) {

        validateNotNull(first, second);
        validateSameMeasurementType(first, second);

        try {

            QuantityModel<IMeasurable> q1 = toModel(first);
            QuantityModel<IMeasurable> q2 = toModel(second);

            validateArithmeticSupported(q1.getUnit());

            double base1 = toBaseValue(q1);
            double base2 = toBaseValue(q2);

            double resultBase = base1 - base2;

            IMeasurable resultUnit = resolveResultUnit(q1, target, first);
            double resultValue = resultUnit.fromBase(resultBase);

            QuantityDTO result =
                    new QuantityDTO(resultValue, mapApplicationUnitToDto(resultUnit));

            repository.save(
                    new QuantityMeasurementEntity(
                            "SUBTRACT",
                            first.toString(),
                            second.toString(),
                            result.toString()));

            return result;

        } catch (Exception e) {
            throw saveAndWrap("SUBTRACT", first, second, e);
        }
    }

    // ---------------- DIVIDE ----------------

    @Override
    public QuantityDTO divide(QuantityDTO first, QuantityDTO second) {

        validateNotNull(first, second);
        validateSameMeasurementType(first, second);

        try {

            QuantityModel<IMeasurable> q1 = toModel(first);
            QuantityModel<IMeasurable> q2 = toModel(second);

            validateArithmeticSupported(q1.getUnit());

            double base1 = toBaseValue(q1);
            double base2 = toBaseValue(q2);

            if (Math.abs(base2) < 0.0000001) {
                throw new QuantityMeasurementException("Division by zero");
            }

            double result = base1 / base2;

            repository.save(
                    new QuantityMeasurementEntity(
                            "DIVIDE",
                            first.toString(),
                            second.toString(),
                            String.valueOf(result)));

            return new QuantityDTO(result, first.getUnit());

        } catch (Exception e) {
            throw saveAndWrap("DIVIDE", first, second, e);
        }
    }

    // ---------------- HISTORY ----------------

    @Override
    public List<QuantityMeasurementEntity> getHistory() {
        return repository.findAll();
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private QuantityModel<IMeasurable> toModel(QuantityDTO dto) {
        return new QuantityModel<>(dto.getValue(), mapDtoUnitToApplicationUnit(dto));
    }

    private double toBaseValue(QuantityModel<IMeasurable> model) {
        return model.getUnit().toBase(model.getValue());
    }

    private IMeasurable resolveResultUnit(
            QuantityModel<IMeasurable> firstModel,
            QuantityDTO target,
            QuantityDTO first) {

        if (target == null) {
            return firstModel.getUnit();
        }

        validateSameMeasurementType(first, target);
        return mapDtoUnitToApplicationUnit(target);
    }

    private IMeasurable mapDtoUnitToApplicationUnit(QuantityDTO dto) {

        String type = dto.getMeasurementType();
        String unit = dto.getUnitName();

        switch (type) {

            case "Length":
                return LengthUnit.valueOf(unit.toUpperCase());

            case "Weight":
                return WeightUnit.valueOf(unit.toUpperCase());

            case "Volume":
                return VolumeUnit.valueOf(unit.toUpperCase());

            case "Temperature":
                return TemperatureUnit.valueOf(unit.toUpperCase());

            default:
                throw new QuantityMeasurementException(
                        "Unsupported measurement type: " + type);
        }
    }

    private QuantityDTO.IMeasurableUnit mapApplicationUnitToDto(IMeasurable unit) {

        String type = unit.getMeasurementType();
        String name = unit.getUnitName();

        switch (type) {

            case "Length":
                return QuantityDTO.LengthUnit.valueOf(name);

            case "Weight":
                return QuantityDTO.WeightUnit.valueOf(name);

            case "Volume":
                return QuantityDTO.VolumeUnit.valueOf(name);

            case "Temperature":
                return QuantityDTO.TemperatureUnit.valueOf(name);

            default:
                throw new QuantityMeasurementException(
                        "Unsupported unit: " + name);
        }
    }

    private void validateNotNull(QuantityDTO a, QuantityDTO b) {

        if (a == null || b == null) {
            throw new QuantityMeasurementException("QuantityDTO cannot be null");
        }
    }

    private void validateSameMeasurementType(QuantityDTO a, QuantityDTO b) {

        if (!a.getMeasurementType().equals(b.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    "Different measurement types not allowed");
        }
    }

    private void validateArithmeticSupported(IMeasurable unit) {

        if (!(unit instanceof SupportsArithmetic)) {

            throw new QuantityMeasurementException(
                    unit.getMeasurementType() +
                    " does not support arithmetic operations");
        }
    }

    private QuantityMeasurementException saveAndWrap(
            String operation,
            QuantityDTO first,
            QuantityDTO second,
            Exception e) {

        repository.save(
                new QuantityMeasurementEntity(
                        operation,
                        first != null ? first.toString() : null,
                        second != null ? second.toString() : null,
                        null,
                        true,
                        e.getMessage()));

        return new QuantityMeasurementException(e.getMessage(), e);
    }
}
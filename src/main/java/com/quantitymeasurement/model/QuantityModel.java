package com.quantitymeasurement.model;

import com.quantitymeasurement.interfaces.IMeasurable;

public class QuantityModel<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public String getMeasurementType() {
        return unit.getMeasurementType();
    }

    @Override
    public String toString() {
        return "QuantityModel(" + value + ", " + unit.getUnitName() + ")";
    }
}
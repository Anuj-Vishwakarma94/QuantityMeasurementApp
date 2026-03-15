package com.quantitymeasurement.unit;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.interfaces.SupportsArithmetic;

public enum WeightUnit implements IMeasurable,SupportsArithmetic {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double toKilogramFactor;

    WeightUnit(double toKilogramFactor) {
        this.toKilogramFactor = toKilogramFactor;
    }

    @Override
    public double toBase(double value) {
        return value * toKilogramFactor;
    }

    @Override
    public double fromBase(double baseValue) {
        return baseValue / toKilogramFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "Weight";
    }

    @Override
    public IMeasurable getInstance(String unitName) {

        for (WeightUnit unit : WeightUnit.values()) {

            if (unit.name().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }

        throw new IllegalArgumentException(
                "Invalid weight unit: " + unitName);
    }
}
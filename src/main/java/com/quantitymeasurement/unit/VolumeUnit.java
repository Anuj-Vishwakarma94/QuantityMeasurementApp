package com.quantitymeasurement.unit;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.interfaces.SupportsArithmetic;

public enum VolumeUnit implements IMeasurable,SupportsArithmetic {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double toLitreFactor;

    VolumeUnit(double toLitreFactor) {
        this.toLitreFactor = toLitreFactor;
    }

    @Override
    public double toBase(double value) {
        return value * toLitreFactor;
    }

    @Override
    public double fromBase(double baseValue) {
        return baseValue / toLitreFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "Volume";
    }

    @Override
    public IMeasurable getInstance(String unitName) {

        for (VolumeUnit unit : VolumeUnit.values()) {

            if (unit.name().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }

        throw new IllegalArgumentException(
                "Invalid volume unit: " + unitName);
    }
}
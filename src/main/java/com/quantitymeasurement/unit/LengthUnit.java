package com.quantitymeasurement.unit;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.interfaces.SupportsArithmetic;

public enum LengthUnit implements IMeasurable,SupportsArithmetic {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.393701 / 12.0);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    @Override
    public double toBase(double value) {
        return value * toFeetFactor;
    }

    @Override
    public double fromBase(double baseValue) {
        return baseValue / toFeetFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "Length";
    }

    @Override
    public IMeasurable getInstance(String unitName) {

        for (LengthUnit unit : LengthUnit.values()) {

            if (unit.name().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }

        throw new IllegalArgumentException(
                "Invalid length unit: " + unitName);
    }

	double convertToBaseUnit(double value) {
		// TODO Auto-generated method stub
		return 0;
	}
}
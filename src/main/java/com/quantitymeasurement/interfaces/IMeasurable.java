package com.quantitymeasurement.interfaces;

import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.TemperatureUnit;
import com.quantitymeasurement.unit.VolumeUnit;
import com.quantitymeasurement.unit.WeightUnit;

public interface IMeasurable {

    String getUnitName();

    String getMeasurementType();

    double toBase(double value);

    double fromBase(double baseValue);

    /**
     * Returns concrete unit instance from unit name.
     */
    IMeasurable getInstance(String unitName);

    /**
     * Default conversion logic between units of the same type.
     */
    default double convertTo(double value, IMeasurable targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        if (!this.getMeasurementType()
                .equals(targetUnit.getMeasurementType())) {

            throw new IllegalArgumentException(
                    "Cannot convert between different measurement types");
        }

        double baseValue = this.toBase(value);

        return targetUnit.fromBase(baseValue);
    }

    // ---------------------------------------------------
    // STATIC HELPER FOR UNIT RESOLUTION
    // ---------------------------------------------------

    static IMeasurable getUnit(String unitName) {

        try { return LengthUnit.valueOf(unitName); } catch (Exception ignored) {}

        try { return WeightUnit.valueOf(unitName); } catch (Exception ignored) {}

        try { return VolumeUnit.valueOf(unitName); } catch (Exception ignored) {}

        try { return TemperatureUnit.valueOf(unitName); } catch (Exception ignored) {}

        throw new IllegalArgumentException("Invalid Unit: " + unitName);
    }
}
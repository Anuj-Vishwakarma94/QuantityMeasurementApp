package com.quantitymeasurement.dto;

public class QuantityDTO {

    private final double value;
    private final IMeasurableUnit unit;

    public QuantityDTO(double value, IMeasurableUnit unit) {

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

    public IMeasurableUnit getUnit() {
        return unit;
    }

    public String getUnitName() {
        return unit.getUnitName();
    }

    public String getMeasurementType() {
        return unit.getMeasurementType();
    }

    @Override
    public String toString() {
        return "QuantityDTO(" + value + ", " + unit.getUnitName() + ")";
    }

    // ---------------------------------------------------------
    // DTO Layer Unit Interface
    // ---------------------------------------------------------

    public interface IMeasurableUnit {

        String getUnitName();

        String getMeasurementType();
    }

    // ---------------------------------------------------------
    // LENGTH UNITS
    // ---------------------------------------------------------

    public enum LengthUnit implements IMeasurableUnit {

        FEET, INCHES, YARDS, CENTIMETERS;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "Length";
        }
    }

    // ---------------------------------------------------------
    // WEIGHT UNITS
    // ---------------------------------------------------------

    public enum WeightUnit implements IMeasurableUnit {

        KILOGRAM, GRAM, POUND;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "Weight";
        }
    }

    // ---------------------------------------------------------
    // VOLUME UNITS
    // ---------------------------------------------------------

    public enum VolumeUnit implements IMeasurableUnit {

        LITRE, MILLILITRE, GALLON;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "Volume";
        }
    }

    // ---------------------------------------------------------
    // TEMPERATURE UNITS
    // ---------------------------------------------------------

    public enum TemperatureUnit implements IMeasurableUnit {

        CELSIUS, FAHRENHEIT, KELVIN;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "Temperature";
        }
    }
}
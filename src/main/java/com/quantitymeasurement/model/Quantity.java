package com.quantitymeasurement.model;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.interfaces.SupportsArithmetic;

public final class Quantity<U extends IMeasurable> {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
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

    // =====================================================
    // CONVERSION
    // =====================================================

    public Quantity<U> convertTo(U targetUnit) {

        validateTargetUnit(targetUnit);

        double base = unit.toBase(value);
        double converted = targetUnit.fromBase(base);

        return new Quantity<>(round2(converted), targetUnit);
    }

    // =====================================================
    // ADD
    // =====================================================

    public Quantity<U> add(Quantity<U> other) {

        validateQuantity(other);
        validateArithmeticSupport();

        double otherInThisUnit =
                other.getUnit().convertTo(other.getValue(), this.unit);

        return new Quantity<>(
                round2(this.value + otherInThisUnit),
                this.unit
        );
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {

        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport();

        double thisInTarget =
                this.unit.convertTo(this.value, targetUnit);

        double otherInTarget =
                other.getUnit().convertTo(other.getValue(), targetUnit);

        return new Quantity<>(
                round2(thisInTarget + otherInTarget),
                targetUnit
        );
    }

    // =====================================================
    // SUBTRACT
    // =====================================================

    public Quantity<U> subtract(Quantity<U> other) {

        validateQuantity(other);
        validateArithmeticSupport();

        double otherInThisUnit =
                other.getUnit().convertTo(other.getValue(), this.unit);

        return new Quantity<>(
                round2(this.value - otherInThisUnit),
                this.unit
        );
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {

        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport();

        double thisInTarget =
                this.unit.convertTo(this.value, targetUnit);

        double otherInTarget =
                other.getUnit().convertTo(other.getValue(), targetUnit);

        return new Quantity<>(
                round2(thisInTarget - otherInTarget),
                targetUnit
        );
    }

    // =====================================================
    // DIVIDE
    // =====================================================

    public double divide(Quantity<U> other) {

        validateQuantity(other);
        validateArithmeticSupport();

        double otherInThisUnit =
                other.getUnit().convertTo(other.getValue(), this.unit);

        if (Math.abs(otherInThisUnit) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero");
        }

        return round2(this.value / otherInThisUnit);
    }

    // =====================================================
    // EQUALITY
    // =====================================================

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (!this.unit.getMeasurementType()
                .equals(other.unit.getMeasurementType())) {

            return false;
        }

        double thisBase = this.unit.toBase(this.value);
        double otherBase = other.unit.toBase(other.value);

        return Math.abs(thisBase - otherBase) < EPSILON;
    }

    @Override
    public int hashCode() {

        double baseValue = unit.toBase(value);

        return Double.hashCode(round2(baseValue));
    }

    // =====================================================
    // STRING
    // =====================================================

    @Override
    public String toString() {

        return "Quantity(" +
                value +
                ", " +
                unit.getUnitName() +
                ")";
    }

    // =====================================================
    // VALIDATION METHODS
    // =====================================================

    private void validateQuantity(Quantity<U> other) {

        if (other == null) {
            throw new IllegalArgumentException(
                    "Other quantity cannot be null"
            );
        }

        if (!this.unit.getMeasurementType()
                .equals(other.unit.getMeasurementType())) {

            throw new IllegalArgumentException(
                    "Measurement types must match"
            );
        }
    }

    private void validateTargetUnit(U targetUnit) {

        if (targetUnit == null) {
            throw new IllegalArgumentException(
                    "Target unit cannot be null"
            );
        }

        if (!this.unit.getMeasurementType()
                .equals(targetUnit.getMeasurementType())) {

            throw new IllegalArgumentException(
                    "Target unit must be of same measurement type"
            );
        }
    }

    private void validateArithmeticSupport() {

        if (!(unit instanceof SupportsArithmetic)) {

            throw new UnsupportedOperationException(
                    unit.getMeasurementType() +
                    " does not support arithmetic operations"
            );
        }
    }

    // =====================================================
    // ROUNDING
    // =====================================================

    private double round2(double number) {

        return Math.round(number * 100.0) / 100.0;
    }
}
package com.quantitymeasurement.model;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.WeightUnit;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityConversionTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testConversion_FeetToInches() {
        Quantity<LengthUnit> result =
                new Quantity<>(1.0, LengthUnit.FEET)
                        .convertTo(LengthUnit.INCHES);

        assertEquals(12.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }

    @Test
    public void testConversion_InchesToFeet() {
        Quantity<LengthUnit> result =
                new Quantity<>(24.0, LengthUnit.INCHES)
                        .convertTo(LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    public void testConversion_YardsToInches() {
        Quantity<LengthUnit> result =
                new Quantity<>(1.0, LengthUnit.YARDS)
                        .convertTo(LengthUnit.INCHES);

        assertEquals(36.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_InchesToYards() {
        Quantity<LengthUnit> result =
                new Quantity<>(72.0, LengthUnit.INCHES)
                        .convertTo(LengthUnit.YARDS);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_CentimetersToInches() {
        Quantity<LengthUnit> result =
                new Quantity<>(2.54, LengthUnit.CENTIMETERS)
                        .convertTo(LengthUnit.INCHES);

        assertEquals(1.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_FeetToYards() {
        Quantity<LengthUnit> result =
                new Quantity<>(6.0, LengthUnit.FEET)
                        .convertTo(LengthUnit.YARDS);

        assertEquals(2.0, result.getValue(), EPSILON);
    }

    @Test
    public void testConversion_RoundTrip_PreservesValue() {

        Quantity<LengthUnit> original =
                new Quantity<>(3.0, LengthUnit.FEET);

        Quantity<LengthUnit> converted =
                original.convertTo(LengthUnit.INCHES)
                        .convertTo(LengthUnit.FEET);

        assertEquals(original, converted);
    }

    @Test
    public void testConversion_ZeroValue() {

        Quantity<LengthUnit> result =
                new Quantity<>(0.0, LengthUnit.FEET)
                        .convertTo(LengthUnit.INCHES);

        assertEquals(new Quantity<>(0.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testConversion_NegativeValue() {

        Quantity<LengthUnit> result =
                new Quantity<>(-1.0, LengthUnit.FEET)
                        .convertTo(LengthUnit.INCHES);

        assertEquals(new Quantity<>(-12.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testConversion_InvalidUnit_Throws() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(1.0, LengthUnit.FEET).convertTo(null)
        );
    }

    @Test
    public void testConversion_NaNOrInfinite_Throws() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.INCHES)
        );
    }

    @Test
    public void testConversion_SameUnit() {

        Quantity<LengthUnit> result =
                new Quantity<>(5.0, LengthUnit.FEET)
                        .convertTo(LengthUnit.FEET);

        assertEquals(new Quantity<>(5.0, LengthUnit.FEET), result);
    }

    // ---------- LengthUnit base conversion checks ----------

    @Test
    public void testConvertToBaseUnit_Feet() {
        assertEquals(5.0,
                LengthUnit.FEET.toBase(5.0),
                EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_Inches() {
        assertEquals(1.0,
                LengthUnit.INCHES.toBase(12.0),
                EPSILON);
    }

    @Test
    public void testConvertToBaseUnit_Yards() {
        assertEquals(3.0,
                LengthUnit.YARDS.toBase(1.0),
                EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_Feet() {
        assertEquals(12.0,
                LengthUnit.INCHES.fromBase(1.0),
                EPSILON);
    }

    @Test
    public void testConvertFromBaseUnit_Yards() {
        assertEquals(1.0,
                LengthUnit.YARDS.fromBase(3.0),
                EPSILON);
    }

    // ---------- Weight conversions ----------

    @Test
    public void testConversion_PoundToKilogram() {

        Quantity<WeightUnit> converted =
                new Quantity<>(2.204624, WeightUnit.POUND)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, converted.getValue(), EPSILON);
        assertEquals(WeightUnit.KILOGRAM, converted.getUnit());
    }

    @Test
    public void testConversion_KilogramToPound() {

        Quantity<WeightUnit> converted =
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.POUND);

        assertEquals(2.204624, converted.getValue(), EPSILON);
        assertEquals(WeightUnit.POUND, converted.getUnit());
    }

    @Test
    public void testConversion_SameUnit_Weight() {

        Quantity<WeightUnit> converted =
                new Quantity<>(5.0, WeightUnit.KILOGRAM)
                        .convertTo(WeightUnit.KILOGRAM);

        assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), converted);
    }

    @Test
    public void testRoundTripConversion_RefactoredDesign() {

        Quantity<LengthUnit> original =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> converted =
                original.convertTo(LengthUnit.CENTIMETERS)
                        .convertTo(LengthUnit.FEET);

        assertEquals(original, converted);
    }
}
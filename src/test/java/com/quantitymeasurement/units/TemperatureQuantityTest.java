package com.quantitymeasurement.units;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.model.Quantity;
import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.TemperatureUnit;
import com.quantitymeasurement.unit.VolumeUnit;
import com.quantitymeasurement.unit.WeightUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureQuantityTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        assertEquals(
                new Quantity<>(0.0, TemperatureUnit.CELSIUS),
                new Quantity<>(0.0, TemperatureUnit.CELSIUS));
    }

    @Test
    public void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        assertEquals(
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT),
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
        assertEquals(
                new Quantity<>(0.0, TemperatureUnit.CELSIUS),
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit() {
        assertEquals(
                new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    public void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
        assertEquals(
                new Quantity<>(-40.0, TemperatureUnit.CELSIUS),
                new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    public void testTemperatureEquality_SymmetricProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    public void testTemperatureEquality_ReflexiveProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(25.0, TemperatureUnit.CELSIUS);
        assertEquals(a, a);
    }

    @Test
    public void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {

        assertEquals(
                122.0,
                new Quantity<>(50.0, TemperatureUnit.CELSIUS)
                        .convertTo(TemperatureUnit.FAHRENHEIT).getValue(),
                EPSILON);

        assertEquals(
                -4.0,
                new Quantity<>(-20.0, TemperatureUnit.CELSIUS)
                        .convertTo(TemperatureUnit.FAHRENHEIT).getValue(),
                EPSILON);

        assertEquals(
                -40.0,
                new Quantity<>(-40.0, TemperatureUnit.CELSIUS)
                        .convertTo(TemperatureUnit.FAHRENHEIT).getValue(),
                EPSILON);
    }

    @Test
    public void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {

        assertEquals(
                50.0,
                new Quantity<>(122.0, TemperatureUnit.FAHRENHEIT)
                        .convertTo(TemperatureUnit.CELSIUS).getValue(),
                EPSILON);

        assertEquals(
                -20.0,
                new Quantity<>(-4.0, TemperatureUnit.FAHRENHEIT)
                        .convertTo(TemperatureUnit.CELSIUS).getValue(),
                EPSILON);

        assertEquals(
                -40.0,
                new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT)
                        .convertTo(TemperatureUnit.CELSIUS).getValue(),
                EPSILON);
    }

    @Test
    public void testTemperatureConversion_RoundTrip_PreservesValue() {

        double original = 37.5;

        Quantity<TemperatureUnit> q =
                new Quantity<>(original, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> converted =
                q.convertTo(TemperatureUnit.FAHRENHEIT)
                        .convertTo(TemperatureUnit.CELSIUS);

        assertEquals(original, converted.getValue(), 1e-4);
    }

    @Test
    public void testTemperatureConversion_SameUnit() {

        Quantity<TemperatureUnit> q =
                new Quantity<>(25.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> out =
                q.convertTo(TemperatureUnit.CELSIUS);

        assertEquals(25.0, out.getValue(), EPSILON);
    }

    @Test
    public void testTemperatureConversion_ZeroValue() {

        Quantity<TemperatureUnit> q =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> out =
                q.convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals(32.0, out.getValue(), EPSILON);
    }

    @Test
    public void testTemperatureConversion_NegativeValues() {

        Quantity<TemperatureUnit> q =
                new Quantity<>(-273.15, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> out =
                q.convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals(-459.67,
                Math.round(out.getValue() * 100.0) / 100.0,
                EPSILON);
    }

    @Test
    public void testTemperatureConversion_LargeValues() {

        Quantity<TemperatureUnit> q =
                new Quantity<>(1000.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> out =
                q.convertTo(TemperatureUnit.FAHRENHEIT);

        assertEquals((1000.0 * 9.0 / 5.0) + 32.0,
                out.getValue(),
                EPSILON);
    }

    @Test
    public void testTemperatureVsLengthIncompatibility() {

        assertFalse(
                new Quantity<>(100.0, TemperatureUnit.CELSIUS)
                        .equals(new Quantity<>(100.0, LengthUnit.FEET)));
    }

    @Test
    public void testTemperatureVsWeightIncompatibility() {

        assertFalse(
                new Quantity<>(50.0, TemperatureUnit.CELSIUS)
                        .equals(new Quantity<>(50.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testTemperatureVsVolumeIncompatibility() {

        assertFalse(
                new Quantity<>(25.0, TemperatureUnit.CELSIUS)
                        .equals(new Quantity<>(25.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testTemperatureUnit_AllConstants() {

        assertNotNull(TemperatureUnit.CELSIUS);
        assertNotNull(TemperatureUnit.FAHRENHEIT);
        assertNotNull(TemperatureUnit.KELVIN);
    }

    @Test
    public void testTemperatureUnit_NameMethod() {

        assertEquals("CELSIUS",
                TemperatureUnit.CELSIUS.getUnitName());

        assertEquals("FAHRENHEIT",
                TemperatureUnit.FAHRENHEIT.getUnitName());
    }

    @Test
    public void testTemperatureNullUnitValidation() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(100.0, null));
    }

    @Test
    public void testTemperatureDifferentValuesInequality() {

        assertNotEquals(
                new Quantity<>(50.0, TemperatureUnit.CELSIUS),
                new Quantity<>(100.0, TemperatureUnit.CELSIUS));
    }

    @Test
    public void testTemperatureConversionPrecision_Epsilon() {

        Quantity<TemperatureUnit> a =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> b =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        double baseA = a.getUnit().toBase(a.getValue());
        double baseB = b.getUnit().toBase(b.getValue());

        assertTrue(Math.abs(baseA - baseB) < EPSILON);
    }

    @Test
    public void testTemperatureEnumImplementsIMeasurable() {

        assertTrue(
                IMeasurable.class.isAssignableFrom(TemperatureUnit.class));
    }

    @Test
    public void testTemperatureIntegrationWithGenericQuantity() {

        Quantity<TemperatureUnit> t =
                new Quantity<>(100.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> converted =
                t.convertTo(TemperatureUnit.KELVIN);

        assertEquals(
                373.15,
                Math.round(converted.getValue() * 1_000_000.0) / 1_000_000.0,
                EPSILON);
    }
}
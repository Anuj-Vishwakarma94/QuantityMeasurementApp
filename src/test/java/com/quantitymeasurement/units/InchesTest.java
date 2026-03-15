package com.quantitymeasurement.units;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.model.Quantity;
import com.quantitymeasurement.unit.LengthUnit;

import static org.junit.jupiter.api.Assertions.*;

public class InchesTest {

    @Test
    void testEquality_SameReference() {

        Quantity<LengthUnit> i1 =
                new Quantity<>(4.0, LengthUnit.INCHES);

        assertEquals(i1, i1);
    }

    @Test
    void testEquality_NonNumericInput() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.INCHES)
        );
    }
}
package com.quantitymeasurement.units;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.model.Quantity;
import com.quantitymeasurement.unit.LengthUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FeetTest {

	// ---------------- FEET TESTS -------------------
	@Test
	void testEquality_SameValue() {
		Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> f2 = new Quantity<>(1.0, LengthUnit.FEET);

		assertTrue(f1.equals(f2), "1.0 ft should be equal to 1.0 ft");
	}

	@Test
	void testEquality_DifferentValue() {
		Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> f2 = new Quantity<>(2.0, LengthUnit.FEET);

		assertFalse(f1.equals(f2), "1.0 ft should NOT be equal to 2.0 ft");
	}

	@Test
	void testEquality_NullComparison() {
		Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);

		assertFalse(f1.equals(null), "1.0 ft should NOT be equal to null");
	}

	@Test
	void testEquality_NonNumericInput() {
		Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);

		// equals takes Object, so "non-numeric input" means comparing with a non-Feet
		// object
		assertFalse(f1.equals("abc"), "Feet should NOT be equal to a non-Feet object");
	}

	@Test
	void testEquality_SameReference() {
		Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);

		assertTrue(f1.equals(f1), "Object should be equal to itself (reflexive)");
	}

}
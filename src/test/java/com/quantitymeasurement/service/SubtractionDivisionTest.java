package com.quantitymeasurement.service;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.model.Quantity;
import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.VolumeUnit;
import com.quantitymeasurement.unit.WeightUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SubtractionDivisionTest {

	private static final double EPS = 1e-6;

	// ---------------- SUBTRACTION ----------------

	@Test
	void testSubtraction_CrossUnit_FeetMinusInches_ImplicitFeet() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES));

		assertEquals(9.50, result.getValue(), EPS);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	void testSubtraction_CrossUnit_FeetMinusInches_ExplicitInches() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);

		assertEquals(114.00, result.getValue(), EPS);
		assertEquals(LengthUnit.INCHES, result.getUnit());
	}

	@Test
	void testSubtraction_Weight_KgMinusGram_ImplicitKg() {
		Quantity<WeightUnit> result = new Quantity<>(10.0, WeightUnit.KILOGRAM)
				.subtract(new Quantity<>(5000.0, WeightUnit.GRAM));

		assertEquals(5.00, result.getValue(), EPS);
		assertEquals(WeightUnit.KILOGRAM, result.getUnit());
	}

	@Test
	void testSubtraction_Volume_LitreMinusMl_ImplicitLitre() {
		Quantity<VolumeUnit> result = new Quantity<>(5.0, VolumeUnit.LITRE)
				.subtract(new Quantity<>(500.0, VolumeUnit.MILLILITRE));

		assertEquals(4.50, result.getValue(), EPS);
		assertEquals(VolumeUnit.LITRE, result.getUnit());
	}

	@Test
	void testSubtraction_ResultNegative() {
		Quantity<LengthUnit> result = new Quantity<>(5.0, LengthUnit.FEET)
				.subtract(new Quantity<>(10.0, LengthUnit.FEET));

		assertEquals(-5.00, result.getValue(), EPS);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	void testSubtraction_ResultZero() {
		Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
				.subtract(new Quantity<>(120.0, LengthUnit.INCHES));

		assertEquals(0.00, result.getValue(), EPS);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	void testSubtraction_NullOther_Throws() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
	}

	@Test
	void testSubtraction_NullTargetUnit_Throws() {
		assertThrows(IllegalArgumentException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testSubtraction_CrossCategory_Throws() {
		Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

		assertThrows(IllegalArgumentException.class, () -> length.subtract((Quantity) weight));
	}

	// ---------------- DIVISION ----------------

	@Test
	void testDivision_SameUnit() {
		double result = new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(5.0, result, EPS);
	}

	@Test
	void testDivision_CrossUnit_Length() {
		double result = new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET));

		assertEquals(1.0, result, EPS);
	}

	@Test
	void testDivision_CrossUnit_Weight() {
		double result = new Quantity<>(2000.0, WeightUnit.GRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM));

		assertEquals(2.0, result, EPS);
	}

	@Test
	void testDivision_RatioLessThanOne() {
		double result = new Quantity<>(5.0, VolumeUnit.LITRE).divide(new Quantity<>(10.0, VolumeUnit.LITRE));

		assertEquals(0.5, result, EPS);
	}

	@Test
	void testDivision_ByZero_Throws() {
		assertThrows(ArithmeticException.class,
				() -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
	}

	@Test
	void testDivision_NullOther_Throws() {
		assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void testDivision_CrossCategory_Throws() {
		Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

		assertThrows(IllegalArgumentException.class, () -> length.divide((Quantity) weight));
	}
}
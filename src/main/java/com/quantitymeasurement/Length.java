package com.quantitymeasurement;

public class Length {

	private final double value;
	private final LengthUnit unit;

	// Enum to represent supported units of length
	public enum LengthUnit {

		FEET(12.0), // Conversion factor: 1 Foot = 12 Inches
		INCHES(1.0); // Conversion factor: 1 Inch = 1 Inch (base unit)

		private final double conversionFactor;

		LengthUnit(double conversionFactor) {
			this.conversionFactor = conversionFactor;
		}

		public double getConversionFactor() {
			return conversionFactor;
		}
	}

	// Constructor to initialize Length with value and unit
	public Length(double value, LengthUnit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}
		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("value must be a finite number");
		}

		this.value = value;
		this.unit = unit;
	}

	// Convert the length to base unit (inches)
	private double convertToBaseUnit() {
		return value * unit.getConversionFactor();
	}

	// Override equals() to compare lengths across units
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Length that = (Length) o;
		return Double.compare(this.convertToBaseUnit(), that.convertToBaseUnit()) == 0;
	}

	// Main method for standalone testing
	public static void main(String[] args) {
		Length length1 = new Length(1.0, LengthUnit.FEET);
		Length length2 = new Length(12.0, LengthUnit.INCHES);
		System.out.println("Are lengths equal? " + length1.equals(length2)); // true
	}
}
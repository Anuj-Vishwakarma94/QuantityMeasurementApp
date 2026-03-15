package com.quantitymeasurement.unit;

import java.util.function.Function;

import com.quantitymeasurement.interfaces.IMeasurable;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS(
            c -> c,
            c -> c
    ),

    FAHRENHEIT(
            f -> (f - 32) * 5.0 / 9.0,
            c -> (c * 9.0 / 5.0) + 32
    ),

    KELVIN(
            k -> k - 273.15,
            c -> c + 273.15
    );

    private final Function<Double, Double> toCelsius;
    private final Function<Double, Double> fromCelsius;

    TemperatureUnit(Function<Double, Double> toCelsius,
                    Function<Double, Double> fromCelsius) {

        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    @Override
    public double toBase(double value) {
        return toCelsius.apply(value);
    }

    @Override
    public double fromBase(double baseValue) {
        return fromCelsius.apply(baseValue);
    }

    @Override
    public String getMeasurementType() {
        return "Temperature";
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public IMeasurable getInstance(String unitName) {

        for (TemperatureUnit unit : TemperatureUnit.values()) {

            if (unit.name().equalsIgnoreCase(unitName)) {
                return unit;
            }
        }

        throw new IllegalArgumentException(
                "Invalid temperature unit: " + unitName);
    }
}
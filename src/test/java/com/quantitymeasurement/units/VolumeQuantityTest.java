package com.quantitymeasurement.units;

import org.junit.jupiter.api.Test;

import com.quantitymeasurement.interfaces.IMeasurable;
import com.quantitymeasurement.model.Quantity;
import com.quantitymeasurement.unit.LengthUnit;
import com.quantitymeasurement.unit.VolumeUnit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class VolumeQuantityTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testEquality_LitreToLitre_SameValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    public void testEquality_LitreToLitre_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(2.0, VolumeUnit.LITRE));
    }

    @Test
    public void testEquality_LitreToMillilitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    public void testEquality_MillilitreToLitre_Symmetric() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_LitreToGallon() {
        Quantity<VolumeUnit> litres =
                new Quantity<>(3.78541, VolumeUnit.LITRE);

        Quantity<VolumeUnit> gallon =
                new Quantity<>(1.0, VolumeUnit.GALLON);

        assertEquals(litres, gallon);
    }

    @Test
    public void testConversion_LitreToMillilitre() {

        Quantity<VolumeUnit> converted =
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE);

        assertEquals(1000.0, converted.getValue(), EPSILON);
    }

    @Test
    public void testConversion_MillilitreToLitre() {

        Quantity<VolumeUnit> converted =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.LITRE);

        assertEquals(1.0, converted.getValue(), EPSILON);
    }

    @Test
    public void testConversion_GallonToLitre() {

        Quantity<VolumeUnit> converted =
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .convertTo(VolumeUnit.LITRE);

        assertEquals(3.78541, converted.getValue(), EPSILON);
    }

    @Test
    public void testAddition_LitrePlusLitre() {

        Quantity<VolumeUnit> sum =
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(2.0, VolumeUnit.LITRE));

        assertEquals(new Quantity<>(3.0, VolumeUnit.LITRE), sum);
    }

    @Test
    public void testAddition_CrossUnit() {

        Quantity<VolumeUnit> sum =
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

        assertEquals(new Quantity<>(2.0, VolumeUnit.LITRE), sum);
    }

    @Test
    public void testAddition_TargetUnit() {

        Quantity<VolumeUnit> sum =
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                                VolumeUnit.MILLILITRE);

        assertEquals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE), sum);
    }

    @Test
    public void testAddition_Commutativity() {

        Quantity<VolumeUnit> a =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> b =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(a.add(b), b.add(a));
    }

    @Test
    public void testAddition_WithZero() {

        Quantity<VolumeUnit> base =
                new Quantity<>(5.0, VolumeUnit.LITRE);

        assertEquals(base,
                base.add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testCrossCategoryPrevention() {

        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    public void testConstructor_NullUnit() {

        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, (VolumeUnit) null));
    }

    @Test
    public void testEquals_NullComparison() {

        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(null));
    }

    @Test
    public void testHashCodeConsistency() {

        Quantity<VolumeUnit> a =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> b =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testCollectionBehavior() {

        Set<Quantity<VolumeUnit>> set = new HashSet<>();

        set.add(new Quantity<>(1.0, VolumeUnit.LITRE));
        set.add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

        assertEquals(1, set.size());
    }

    @Test
    public void testIMeasurableImplementation() {

        IMeasurable unit = VolumeUnit.LITRE;

        assertEquals("LITRE", unit.getUnitName());
        assertEquals("Volume", unit.getMeasurementType());

        assertEquals(1.0, unit.toBase(1.0), EPSILON);
        assertEquals(1.0, unit.fromBase(1.0), EPSILON);
    }

    @Test
    public void testImmutability() {

        Quantity<VolumeUnit> q =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> added =
                q.add(new Quantity<>(1.0, VolumeUnit.LITRE));

        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), q);
        assertEquals(new Quantity<>(2.0, VolumeUnit.LITRE), added);
    }
}
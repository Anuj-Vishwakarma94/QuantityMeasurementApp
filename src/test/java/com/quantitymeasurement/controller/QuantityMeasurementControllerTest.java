package com.quantitymeasurement.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.model.QuantityMeasurementEntity;
import com.quantitymeasurement.service.IQuantityMeasurementService;

public class QuantityMeasurementControllerTest {

    private QuantityMeasurementController controller;
    private TestService service;

    @BeforeEach
    void setUp() {
        service = new TestService();
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void testController_PerformComparison_Success() {

        boolean result = controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

        assertTrue(result);
        assertTrue(service.compareCalled);
    }

    @Test
    void testController_PerformConversion_Success() {

        QuantityDTO result = controller.performConversion(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES));

        assertEquals(12.0, result.getValue(), 1e-6);
        assertTrue(service.convertCalled);
    }

    @Test
    void testController_PerformAddition_Success() {

        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(2.0, result.getValue(), 1e-6);
        assertTrue(service.addCalled);
    }

    @Test
    void testController_PerformAddition_Error() {

        service.throwOnAdd = true;

        assertThrows(
                QuantityMeasurementException.class,
                () -> controller.performAddition(
                        new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                        new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                        new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET)));
    }

    @Test
    void testController_PerformDivision_Success() {

        double result = controller.performDivision(
                new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testController_GetHistory_Success() {

        List<QuantityMeasurementEntity> history = controller.getHistory();

        assertNotNull(history);
        assertTrue(history.isEmpty());
    }

    @Test
    void testController_PerformSubtraction_Success() {

        QuantityDTO result = controller.performSubtraction(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Fake service used only for controller testing
     */
    private static class TestService implements IQuantityMeasurementService {

        boolean compareCalled;
        boolean convertCalled;
        boolean addCalled;
        boolean throwOnAdd;

        @Override
        public QuantityDTO compare(QuantityDTO first, QuantityDTO second) {
            compareCalled = true;
            return new QuantityDTO(1.0, first.getUnit());
        }

        @Override
        public QuantityDTO convert(QuantityDTO source, QuantityDTO target) {
            convertCalled = true;
            return new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        }

        @Override
        public QuantityDTO add(QuantityDTO first, QuantityDTO second, QuantityDTO target) {

            addCalled = true;

            if (throwOnAdd) {
                throw new QuantityMeasurementException("Simulated service failure");
            }

            return new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        }

        @Override
        public QuantityDTO subtract(QuantityDTO first, QuantityDTO second, QuantityDTO target) {
            return new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        }

        @Override
        public QuantityDTO divide(QuantityDTO first, QuantityDTO second) {
            return new QuantityDTO(1.0, first.getUnit());
        }

        @Override
        public List<QuantityMeasurementEntity> getHistory() {
            return new ArrayList<>();
        }
    }
}
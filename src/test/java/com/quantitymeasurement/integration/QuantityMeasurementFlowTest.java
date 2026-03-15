package com.quantitymeasurement.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.model.QuantityMeasurementEntity;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.quantitymeasurement.service.IQuantityMeasurementService;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementFlowTest {

	private QuantityMeasurementController controller;
	private IQuantityMeasurementRepository repository;

	@BeforeEach
	void setUp() {
		repository = QuantityMeasurementCacheRepository.getInstance();
		repository.clear();

		IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
		controller = new QuantityMeasurementController(service);
	}

	@Test
	void testDataFlow_ControllerToService() {
		boolean result = controller.performComparison(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
				new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

		assertTrue(result);
	}

	@Test
	void testDataFlow_ServiceToController() {
		QuantityDTO result = controller.performConversion(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
				new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES));

		assertEquals(12.0, result.getValue(), 1e-6);
		assertEquals("INCHES", result.getUnitName());
	}

	@Test
	void testIntegration_EndToEnd_LengthAddition() {
		QuantityDTO result = controller.performAddition(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
				new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
				new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));
		assertEquals(2.0, result.getValue(), 1e-6);
	}

	@Test
	void testIntegration_EndToEnd_TemperatureUnsupported() {
		assertThrows(QuantityMeasurementException.class,
				() -> controller.performAddition(new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS),
						new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT),
						new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS)));
	}

	@Test
	void testHistoryPersistenceFlow() {
		controller.performComparison(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
				new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));

		List<QuantityMeasurementEntity> history = controller.getHistory();

		assertEquals(1, history.size());
		assertEquals("COMPARE", history.get(0).getOperationType());
	}
}
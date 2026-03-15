package com.quantitymeasurement.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurement.model.QuantityMeasurementEntity;

/**
 * UC15 Repository Layer Test
 *
 * This test verifies the correct behavior of the
 * QuantityMeasurementCacheRepository implementation.
 *
 * Test Coverage: - Saving entities - Retrieving stored history - Clearing
 * repository - Append-based persistence
 *
 * These tests ensure the repository layer behaves independently from service
 * and controller layers.
 */
public class QuantityMeasurementCacheRepositoryTest {

	private QuantityMeasurementCacheRepository repository;

	/**
	 * Reset repository before every test to ensure isolation.
	 */
	@BeforeEach
	void setUp() {
		repository = QuantityMeasurementCacheRepository.getInstance();
		repository.clear();
	}

	/**
	 * Test saving a measurement entity into repository.
	 */
	@Test
	void testRepository_Save_Success() {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true");

		repository.save(entity);

		List<QuantityMeasurementEntity> history = repository.findAll();

		assertEquals(1, history.size());
		assertEquals("COMPARE", history.get(0).getOperationType());
	}

	/**
	 * Test retrieving multiple stored entities.
	 */
	@Test
	void testRepository_FindAll_ReturnsHistory() {

		repository.save(new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true"));

		repository.save(new QuantityMeasurementEntity("CONVERT", "1.0 FEET", null, "12.0 INCHES"));

		List<QuantityMeasurementEntity> history = repository.findAll();

		assertEquals(2, history.size());
	}

	/**
	 * Test clearing repository history.
	 */
	@Test
	void testRepository_Clear_RemovesAllData() {

		repository.save(new QuantityMeasurementEntity("ADD", "1.0 FEET", "12.0 INCHES", "2.0 FEET"));

		repository.clear();

		List<QuantityMeasurementEntity> history = repository.findAll();

		assertTrue(history.isEmpty());
	}

	/**
	 * Test that repository does not accept null entities.
	 */
	@Test
	void testRepository_Save_NullEntity_Error() {

		assertThrows(IllegalArgumentException.class, () -> {
			repository.save(null);
		});
	}

	/**
	 * Test append-based persistence behavior.
	 *
	 * Ensures that saving multiple entities results in multiple stored records.
	 */
	@Test
	void testRepository_AppendPersistence() {

		repository.save(new QuantityMeasurementEntity("COMPARE", "1.0 FEET", "12.0 INCHES", "true"));

		repository.save(new QuantityMeasurementEntity("DIVIDE", "24.0 INCHES", "2.0 FEET", "1.0"));

		List<QuantityMeasurementEntity> history = repository.findAll();

		assertEquals(2, history.size());
	}
}
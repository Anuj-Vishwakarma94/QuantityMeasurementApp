package com.quantitymeasurement;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.dto.QuantityDTO;
import com.quantitymeasurement.exception.QuantityMeasurementException;
import com.quantitymeasurement.model.QuantityMeasurementEntity;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.quantitymeasurement.service.IQuantityMeasurementService;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        IQuantityMeasurementRepository repository =
                QuantityMeasurementCacheRepository.getInstance();

        IQuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        try {

            // ---------------- LENGTH EXAMPLE ----------------

            QuantityDTO length1 =
                    new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);

            QuantityDTO length2 =
                    new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

            boolean comparison =
                    controller.performComparison(length1, length2);

            System.out.println("Comparison Result: " + comparison);

            QuantityDTO convertTarget =
                    new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES);

            QuantityDTO converted =
                    controller.performConversion(length1, convertTarget);

            System.out.println("Conversion Result: " + converted);

            QuantityDTO addTarget =
                    new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET);

            QuantityDTO added =
                    controller.performAddition(length1, length2, addTarget);

            System.out.println("Addition Result: " + added);

            QuantityDTO subtracted =
                    controller.performSubtraction(
                            new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                            new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES),
                            new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET)
                    );

            System.out.println("Subtraction Result: " + subtracted);

            double division =
                    controller.performDivision(
                            new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES),
                            new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET)
                    );

            System.out.println("Division Result: " + division);


            // ---------------- TEMPERATURE ERROR EXAMPLE ----------------

            try {

                QuantityDTO temp1 =
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);

                QuantityDTO temp2 =
                        new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);

                controller.performAddition(
                        temp1,
                        temp2,
                        new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS)
                );

            } catch (QuantityMeasurementException e) {

                System.out.println("Expected Error: " + e.getMessage());
            }


            // ---------------- OPERATION HISTORY ----------------

            System.out.println("\n--- Operation History ---");

            for (QuantityMeasurementEntity entity : controller.getHistory()) {
                System.out.println(entity);
            }

        } catch (Exception e) {

            System.out.println("Application Error: " + e.getMessage());
        }
    }
}
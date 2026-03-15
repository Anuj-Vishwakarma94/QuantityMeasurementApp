package com.quantitymeasurement.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operationType;
    private String firstOperand;
    private String secondOperand;
    private String result;
    private boolean error;
    private String errorMessage;
    private LocalDateTime timestamp;

    public QuantityMeasurementEntity(String operationType, String firstOperand, String result) {

        this.operationType = operationType;
        this.firstOperand = firstOperand;
        this.secondOperand = null;
        this.result = result;
        this.error = false;
        this.errorMessage = null;
        this.timestamp = LocalDateTime.now();
    }

    public QuantityMeasurementEntity(
            String operationType,
            String firstOperand,
            String secondOperand,
            String result) {

        this.operationType = operationType;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.result = result;
        this.error = false;
        this.errorMessage = null;
        this.timestamp = LocalDateTime.now();
    }

    public QuantityMeasurementEntity(
            String operationType,
            String firstOperand,
            String secondOperand,
            String result,
            boolean error,
            String errorMessage) {

        this.operationType = operationType;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.result = result;
        this.error = error;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public String getOperationType() {
        return operationType;
    }

    public String getFirstOperand() {
        return firstOperand;
    }

    public String getSecondOperand() {
        return secondOperand;
    }

    public String getResult() {
        return result;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {

        if (error) {
            return "[ERROR] time=" + timestamp +
                    ", operation=" + operationType +
                    ", first=" + firstOperand +
                    ", second=" + secondOperand +
                    ", message=" + errorMessage;
        }

        return "[SUCCESS] time=" + timestamp +
                ", operation=" + operationType +
                ", first=" + firstOperand +
                ", second=" + secondOperand +
                ", result=" + result;
    }
}
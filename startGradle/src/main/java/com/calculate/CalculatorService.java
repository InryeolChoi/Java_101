package com.calculate;

public class CalculatorService {
    private final Calculator calculator;

    public CalculatorService(Calculator calculator) {
        this.calculator = calculator;
    }

    public double performAddition(double a, double b) {
        return calculator.add(a, b);
    }

    public double performSubtraction(double a, double b) {
        return calculator.subtract(a, b);
    }

    public double performMultiplication(double a, double b) {
        return calculator.multiply(a, b);
    }

    public double performDivision(double a, double b) {
        return calculator.divide(a, b);
    }
}
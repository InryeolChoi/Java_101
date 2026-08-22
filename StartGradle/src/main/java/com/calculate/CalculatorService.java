package com.calculate;
import java.util.function.DoubleUnaryOperator;

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

    public double performPower(double base, double exponent) {
        return calculator.power(base, exponent);
    }

    public double performExp(double value) {
        return calculator.exp(value);
    }

    public double performLog(double value) {
        return calculator.log(value);
    }

    public double performSin(double angle) {
        return calculator.sin(angle);
    }

    public double performCos(double angle) {
        return calculator.cos(angle);
    }

    public double performTan(double angle) {
        return calculator.tan(angle);
    }

    public double performIntegration(DoubleUnaryOperator function, double a, double b, int n) {
        return calculator.integrate(function, a, b, n);
    }

}
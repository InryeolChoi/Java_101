package com.calculate;

import java.util.function.DoubleUnaryOperator;

public class Calculator {
    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        return a / b;
    }

    // 새로운 기능들
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public double exp(double value) {
        return Math.exp(value);
    }

    public double log(double value) {
        if (value <= 0) {
            throw new ArithmeticException("Logarithm of non-positive numbers is undefined.");
        }
        return Math.log(value);
    }

    public double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public double tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    public double integrate(DoubleUnaryOperator function, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (function.applyAsDouble(a) + function.applyAsDouble(b));
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += function.applyAsDouble(x);
        }
        return sum * h;
    }

}

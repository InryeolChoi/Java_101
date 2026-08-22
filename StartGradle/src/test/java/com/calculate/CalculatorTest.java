package com.calculate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        assertEquals(5, calculator.add(2, 3), "Addition should work.");
    }

    @Test
    public void testSubtract() {
        assertEquals(1, calculator.subtract(5, 4), "Subtraction should work.");
    }

    @Test
    public void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3), "Multiplication should work.");
    }

    @Test
    public void testDivide() {
        assertEquals(2, calculator.divide(6, 3), "Division should work.");
    }

    @Test
    public void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0), "Division by zero should throw an exception.");
    }

    @Test
    public void testPower() {
        assertEquals(8, calculator.power(2, 3), "Power function should work.");
    }

    @Test
    public void testExp() {
        assertEquals(Math.exp(2), calculator.exp(2), "Exponential function should work.");
    }

    @Test
    public void testLog() {
        assertEquals(Math.log(2), calculator.log(2), "Logarithm function should work.");
    }

    @Test
    public void testSin() {
        assertEquals(Math.sin(Math.toRadians(30)), calculator.sin(30), "Sine function should work.");
    }

    @Test
    public void testCos() {
        assertEquals(Math.cos(Math.toRadians(60)), calculator.cos(60), "Cosine function should work.");
    }

    @Test
    public void testTan() {
        assertEquals(Math.tan(Math.toRadians(45)), calculator.tan(45), "Tangent function should work.");
    }

    @Test
    public void testIntegration() {
        // Integration of f(x) = x^2 from 0 to 1 should be 1/3
        assertEquals(1.0 / 3, calculator.integrate(x -> x * x, 0, 1, 1000), 0.01, "Integration should work.");
    }
}
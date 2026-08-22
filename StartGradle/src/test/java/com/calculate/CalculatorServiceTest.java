package com.calculate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorServiceTest {

    private final Calculator calculator = new Calculator();
    private final CalculatorService service = new CalculatorService(calculator);

    @Test
    public void testPerformAddition() {
        assertEquals(5, service.performAddition(2, 3), "Addition should work.");
    }

    @Test
    public void testPerformSubtraction() {
        assertEquals(1, service.performSubtraction(5, 4), "Subtraction should work.");
    }

    @Test
    public void testPerformMultiplication() {
        assertEquals(6, service.performMultiplication(2, 3), "Multiplication should work.");
    }

    @Test
    public void testPerformDivision() {
        assertEquals(2, service.performDivision(6, 3), "Division should work.");
    }

    @Test
    public void testPerformPower() {
        assertEquals(8, service.performPower(2, 3), "Power function should work.");
    }

    @Test
    public void testPerformExp() {
        assertEquals(Math.exp(2), service.performExp(2), "Exponential function should work.");
    }

    @Test
    public void testPerformLog() {
        assertEquals(Math.log(2), service.performLog(2), "Logarithm function should work.");
    }

    @Test
    public void testPerformSin() {
        assertEquals(Math.sin(Math.toRadians(30)), service.performSin(30), "Sine function should work.");
    }

    @Test
    public void testPerformCos() {
        assertEquals(Math.cos(Math.toRadians(60)), service.performCos(60), "Cosine function should work.");
    }

    @Test
    public void testPerformTan() {
        assertEquals(Math.tan(Math.toRadians(45)), service.performTan(45), "Tangent function should work.");
    }

    @Test
    public void testPerformIntegration() {
        // Integration of f(x) = x^2 from 0 to 1 should be 1/3
        assertEquals(1.0 / 3, service.performIntegration(x -> x * x, 0, 1, 1000), 0.01, "Integration should work.");
    }
}
package com.calculate;

import java.util.Scanner;

public class CalculatorApp {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        CalculatorService service = new CalculatorService(calculator);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter first number:");
        double a = scanner.nextDouble();
        System.out.println("Enter second number:");
        double b = scanner.nextDouble();

        System.out.println("Select operation:");
        System.out.println("1 - Add");
        System.out.println("2 - Subtract");
        System.out.println("3 - Multiply");
        System.out.println("4 - Divide");
        System.out.println("5 - Power");
        System.out.println("6 - Exponential");
        System.out.println("7 - Logarithm");
        System.out.println("8 - Sine");
        System.out.println("9 - Cosine");
        System.out.println("10 - Tangent");
        System.out.println("11 - Integration");

        int choice = scanner.nextInt();

        double result = 0;
        switch (choice) {
            case 1:
                result = service.performAddition(a, b);
                break;
            case 2:
                result = service.performSubtraction(a, b);
                break;
            case 3:
                result = service.performMultiplication(a, b);
                break;
            case 4:
                result = service.performDivision(a, b);
                break;
            case 5:
                result = service.performPower(a, b);
                break;
            case 6:
                result = service.performExp(a);
                break;
            case 7:
                result = service.performLog(a);
                break;
            case 8:
                result = service.performSin(a);
                break;
            case 9:
                result = service.performCos(a);
                break;
            case 10:
                result = service.performTan(a);
                break;
            case 11:
                System.out.println("Enter the number of intervals for integration:");
                int n = scanner.nextInt();
                result = service.performIntegration(x -> x * x, a, b, n); // 예: f(x) = x^2
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("The result is: " + result);
    }
}
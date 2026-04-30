package com.ssnagin.collectionmanager.math.lab3;

public class MathematicsLab3 {

    public static double trapezoid(Func1D function, double a, double b, double n) {
        double h = (b - a) / n;
        double sum = (function.apply(a) + function.apply(b)) / 2.0;
        for (int i = 0; i < n; i++) {
            sum += function.apply(a + i * h);
        }
        return h * sum;
    }

    public static double simpsons(Func1D function, double a, double b, double n) {

        if (n % 2 == 1)
            n = n + 1;

        double h = (b - a) / n;

        double sumOdd = 0.0;
        double sumEven = 0;

        for (int i = 1; i <= n; i+=2) {
            sumOdd += function.apply(a + i * h);
        }
        for (int i = 2; i <= n; i+=2) {
            sumEven += function.apply(a + i * h);
        }
        return (h / 3.0) * (function.apply(a) + function.apply(b) + 4 * sumOdd + 2 * sumEven);
    }

    public static double middleRectangles(Func1D function, double a, double b, double n) {
        double h = (b - a) / n;

        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += function.apply(a + (i - 0.5) * h);
        }

        return h * sum;
    }

    public static double leftRectangles(Func1D function, double a, double b, double n) {
        double h = (b - a) / n;

        double S = 0;
        for (int i = 0; i < n; i++) {
            S += function.apply(a + i * h);
        }

        return h * S;
    }

    public static double rightRectangles(Func1D function, double a, double b, double n) {
        double h = (b - a) / n;

        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += function.apply(a + i * h);
        }

        return h * sum;
    }
}

package com.ssnagin.collectionmanager.math.lab3;

public class MathematicsLab3 {

    public static double leftRectangles(Func1D function, double a, double b, double n) {
        double h = (b - a) / n;
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += function.apply(a + i * h);
        }

        return h * sum;
    }

    public static double righttRectangles(double a, double b, double n) {
        return 0;
    }

    public static double middleRectangles(double a, double b, double n) {
        return 0;
    }

    public static double trapezoid(double a, double b, double n) {
        return 0;
    }

    public static double simpsons(double a, double b, double n) {
        return 0;
    }
}

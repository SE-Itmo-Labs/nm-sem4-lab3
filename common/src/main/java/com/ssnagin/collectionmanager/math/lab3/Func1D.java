package com.ssnagin.collectionmanager.math.lab3;

public abstract class Func1D {
    public abstract double apply(double x);

    public double safeApply(double x) {
        try {
            return apply(x);
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public double safeAbsApply(double x) {
        try {
            double y = apply(x);
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                return Double.POSITIVE_INFINITY;
            }
            return Math.abs(y);
        } catch (Exception e) {
            return Double.POSITIVE_INFINITY;
        }
    }

    public abstract double derivative(double x);

    public abstract double secondDerivative(double x);

    public abstract String description();
}
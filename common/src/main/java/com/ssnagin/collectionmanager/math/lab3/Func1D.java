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

    public abstract double derivative(double x);

    public abstract double secondDerivative(double x);

    public abstract String description();
}
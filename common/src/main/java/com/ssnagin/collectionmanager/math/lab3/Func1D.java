package com.ssnagin.collectionmanager.math.lab3;

public interface Func1D {
    double apply(double x);

    double derivative(double x);

    double secondDerivative(double x);

    String description();
}
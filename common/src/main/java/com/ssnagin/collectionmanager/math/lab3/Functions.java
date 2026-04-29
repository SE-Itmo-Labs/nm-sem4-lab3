package com.ssnagin.collectionmanager.math.lab3;

import java.util.Map;

public class Functions {

    /* f(x) = x^2 + 3x - 5 */
    public static final Func1D FUNC_1 = new Func1D() {
        @Override
        public double apply(double x) {
            return 0;
        }

        // No method required
        @Override
        public double derivative(double x) {
            return 0;
        }

        // No method required
        @Override
        public double secondDerivative(double x) {
            return 0;
        }

        @Override
        public String description() {
            return "x^2 + 3x - 5";
        }
    };

    // cos(x)
    Func1D FUNC_2 = new Func1D() {
        @Override
        public double apply(double x) {
            return 0;
        }

        // No method required
        @Override
        public double derivative(double x) {
            return 0;
        }

        // No method required
        @Override
        public double secondDerivative(double x) {
            return 0;
        }

        @Override
        public String description() {
            return "cos(x)";
        }
    };

    // e^x
    Func1D FUNC_3 = new Func1D() {
        @Override
        public double apply(double x) {
            return 0;
        }

        // No method required
        @Override
        public double derivative(double x) {
            return 0;
        }

        // No method required
        @Override
        public double secondDerivative(double x) {
            return 0;
        }

        @Override
        public String description() {
            return "e^x";
        }
    };
}

/*
        functionsSet.add("x^2 + 3x - 5");
        functionsSet.add("cos(x)");
        functionsSet.add("e^x");
        functionsSet.add("sqrt(x^2 + 1)");
        functionsSet.add("|x| + 2");
        functionsSet.add("1 / sqrt(|x|)");
        functionsSet.add("1 / x^2");
        functionsSet.add("1 / |x-1|^(2/3)");
 */
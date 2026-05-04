package com.ssnagin.collectionmanager.math.lab3;

import java.util.Map;

public class Functions {

    /* f(x) = x^2 + 3x - 5 */
    public static final Func1D FUNC_1 = new Func1D() {
        @Override
        public double apply(double x) {
            return x * x + 3 * x - 5;
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
    public static final Func1D FUNC_2 = new Func1D() {
        @Override
        public double apply(double x) {
            return Math.cos(x);
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
    public static final Func1D FUNC_3 = new Func1D() {
        @Override
        public double apply(double x) {
            return Math.exp(x);
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

    // sqrt(x^2 + 1)
    public static final Func1D FUNC_4 = new Func1D() {
        @Override
        public double apply(double x) {
            return Math.sqrt(x * x + 1);
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
            return "sqrt(x^2 + 1)";
        }
    };

    // |x| + 2
    public static final Func1D FUNC_5 = new Func1D() {
        @Override
        public double apply(double x) {
            return Math.abs(x) + 2;
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
            return "|x| + 2";
        }
    };

    // 1 / sqrt(|x|)
    public static final Func1D FUNC_6 = new Func1D() {
        @Override
        public double apply(double x) {
            return 1.0 / Math.sqrt(Math.abs(x));
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
            return "1 / sqrt(|x|)";
        }
    };

    // 1 / x^2
    public static final Func1D FUNC_7 = new Func1D() {
        @Override
        public double apply(double x) {
            return 1.0 / (x * x);
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
            return "1 / x^2";
        }
    };

    // 1 / |x-1|^(2/3)
    public static final Func1D FUNC_8 = new Func1D() {
        @Override
        public double apply(double x) {
            return 1.0 / Math.pow(Math.abs(x - 1), 2.0 / 3.0);
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
            return "1 / |x-1|^(2/3)";
        }
    };

    public static final Func1D FUNC_9 = new Func1D() {

        @Override
        public double apply(double x) {
            return 1 / x;
        }

        @Override
        public double derivative(double x) {
            return 0;
        }

        @Override
        public double secondDerivative(double x) {
            return 0;
        }

        @Override
        public String description() {
            return "1 / x";
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
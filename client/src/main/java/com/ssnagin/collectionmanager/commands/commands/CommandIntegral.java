package com.ssnagin.collectionmanager.commands.commands;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.math.lab3.Func1D;
import com.ssnagin.collectionmanager.math.lab3.Functions;
import com.ssnagin.collectionmanager.math.lab3.Lab3Form;
import com.ssnagin.collectionmanager.math.lab3.MathematicsLab3;
import com.ssnagin.collectionmanager.reflection.Reflections;
import com.ssnagin.collectionmanager.scripts.ScriptManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class CommandIntegral extends UserCommand {

    private ScriptManager scriptManager;

    protected TreeSet<String> commandSet = new TreeSet<>();
    protected TreeSet<String> functionsSet = new TreeSet<>();


    public CommandIntegral(String name, String description, ScriptManager scriptManager) {
        super(name, description);

        this.scriptManager = scriptManager;

        commandSet.add("leftRectangles");
        commandSet.add("rightRectangles");
        commandSet.add("middleRectangles");
        commandSet.add("trapezoid");
        commandSet.add("simpsons");

        functionsSet.add("x^2 + 3x - 5");
        functionsSet.add("cos(x)");
        functionsSet.add("e^x");
        functionsSet.add("sqrt(x^2 + 1)");
        functionsSet.add("|x| + 2");
        functionsSet.add("1 / sqrt(|x|)");
        functionsSet.add("1 / x^2");
        functionsSet.add("1 / |x-1|^(2/3)");
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Scanner scanner = this.scriptManager.getCurrentScanner();

        if (parsedString.getArguments().isEmpty()) {
            showUsage(parsedString);
            return ApplicationStatus.RUNNING;
        }

        String command = parsedString.getArguments().get(0);
        double result = 0;

        if (!commandSet.contains(command)) {

            ClientConsole.error("Wrong method!\n");

            showUsage(parsedString);
            return ApplicationStatus.RUNNING;
        }

        String functionName;
        Func1D function;

        try {
            ClientConsole.print("Введите функцию (доступные см в usage): \n       | ");
            functionName = scanner.nextLine();

            function = switch (functionName) {
                case "x^2 + 3x - 5" -> Functions.FUNC_1;
                case "cos(x)" -> Functions.FUNC_2;
                case "e^x" -> Functions.FUNC_3;
                case "sqrt(x^2 + 1)" -> Functions.FUNC_4;
                case "|x| + 2" -> Functions.FUNC_5;
                case "1 / sqrt(|x|)" -> Functions.FUNC_6;
                case "1 / x^2" -> Functions.FUNC_7;
                case "1 / |x-1|^(2/3)" -> Functions.FUNC_8;
                default -> throw new IllegalArgumentException("Неизвестная функция: " + functionName);
            };

        } catch (IllegalArgumentException ex) {
            ClientConsole.error(ex.toString());
            return ApplicationStatus.RUNNING;
        }

        try {
            Lab3Form form = Reflections.parseModel(Lab3Form.class, scanner);
/*
            switch (command) {
                case "leftRectangles":
                    result = MathematicsLab3.leftRectangles(function, form.getXStart(), form.getXEnd(), form.getK());
                    break;
                    case "rightRectangles":
                        result = MathematicsLab3.rightRectangles(function, form.getXStart(), form.getXEnd(), form.getK());
                        break;
 */
            return integrate(command, function, form.getXStart(), form.getXEnd(), form.getEpsilon());

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
            ClientConsole.error(ex.toString());
        }

        return ApplicationStatus.RUNNING;
    }

    protected static ApplicationStatus integrate(String method, Func1D function, double x_start, double x_end, double epsilon) {

        if (x_start >= x_end) {
            ClientConsole.error("Недопустимые границы, левая больше или равна правой");
            return ApplicationStatus.RUNNING;
        }

        if (!definedArea(function, x_start, x_end)) {
            ClientConsole.error("В слишком многих промежутках функция не определена");
            return ApplicationStatus.RUNNING;
        }

        List<Double> singularities = weirdDots(function, x_start, x_end);



        return ApplicationStatus.RUNNING;
    }

    public static boolean definedArea(Func1D f, double a, double b) {

        int steps = 400;
        double dx = (b - a) / steps;
        int badCount = 0;

        for (int i = 0; i <= steps; i++) {

            double x = a + i * dx;
            double y = f.safeApply(x);
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                badCount++;
            }

        }

        double badRatio = (double) badCount / steps;

        ClientConsole.log("definedArea : " + (1 - badRatio) * 100 + "% (" + badRatio*100 + "%)");

        return badRatio <= 0.35;
    }

    private static double refinePole(Func1D f, double left, double right) {
        double l = Math.max(left, left); // coerceAtLeast
        double r = right;

        if (r - l < 1e-14) {
            return (l + r) / 2.0;
        }

        double bestPos = (l + r) / 2.0;
        double bestAbs = 0.0;

        for (int iter = 0; iter < 200; iter++) {
            double m1 = l + (r - l) / 3.0;
            double m2 = r - (r - l) / 3.0;

            double v1 = f.apply(m1);
            double v2 = f.apply(m2);

            if (v1 > bestAbs) {
                bestAbs = v1;
                bestPos = m1;
            }
            if (v2 > bestAbs) {
                bestAbs = v2;
                bestPos = m2;
            }

            if (v1 < v2) {
                l = m1;
            } else {
                r = m2;
            }
        }

        l = Math.max(bestPos - (right - left) * 0.08, left);
        r = Math.min(bestPos + (right - left) * 0.08, right);

        for (int iter = 0; iter < 100; iter++) {
            double m = (l + r) / 2.0;
            double vm = f.apply(m);

            if (vm > bestAbs) {
                bestAbs = vm;
                bestPos = m;
            }

            double leftTest = f.apply(m - (r - l) * 0.1);
            double rightTest = f.apply(m + (r - l) * 0.1);

            if (leftTest > rightTest) {
                r = m;
            } else {
                l = m;
            }
        }

        return bestPos;
    }

    public static List<Double> weirdDots(Func1D f, double a, double b) {
        List<Double> singularities = new ArrayList<>();
        int steps = 50_000;
        double dx = (b - a) / steps;
        double radius = 0.0001;

        double prevX = a;
        double prevY = f.apply(a);

        for (int i = 0; i <= steps; i++) {
            double x = a + i * dx;
            double y = f.apply(x);

            boolean foundPotentialPole = clarifyWeirdDot(y, prevY);

            if (foundPotentialPole) {

                // Clarify the coordinate in [prevX-2dx, x+2dx]

                double searchLeft = Math.max(prevX - dx * 2, a);
                double searchRight = Math.min(x + dx * 2, b);

                double refined = refinePole(f, searchLeft, searchRight);

                double rounded = Math.round(refined * 1e8) / 1e8;

                if (Math.abs(rounded) < 1e-9)
                    rounded = 0.0;

                boolean alreadyExists = false;

                for (double existing : singularities) {
                    if (Math.abs(existing - rounded) < radius) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists) {
                    singularities.add(rounded);
                }
            }

            prevY = y;
            prevX = x;
        }

        singularities.sort(Double::compare);
        return singularities;
    }

    private static boolean clarifyWeirdDot(double y, double prevY) {
        boolean foundPotentialPole = false;

        // inf or undefined value
        if (Double.isInfinite(y) || Double.isNaN(y) || Math.abs(y) > 1e5) {
            foundPotentialPole = true;
        }
        // Huge difference
        else if (Double.isFinite(prevY) && Math.abs(y - prevY) > 1000.0 * Math.max(1.0, Math.abs(prevY))) {
            foundPotentialPole = true;
        }
        // sign switch
        else if (Double.isFinite(prevY) && Double.isFinite(y)) {
            if (y * prevY < 0.0 && Math.abs(y) > 50.0 && Math.abs(prevY) > 50.0) {
                foundPotentialPole = true;
            }
        }
        return foundPotentialPole;
    }

    private static boolean isCauchyPrincipalValuePoint(Func1D f, double s) {
        int amount = 0;
        // Тестовые расстояния от точки разрыва
        double[] testPoints = {0.1, 0.05, 0.02, 0.01, 0.005};

        for (double h : testPoints) {

            double left = f.safeApply(s - h);
            double right = f.safeApply(s + h);

            // skip if one of values is invalid
            if (!Double.isFinite(left) || !Double.isFinite(right) ||
                    Double.isNaN(left) || Double.isNaN(right)) {
                continue;
            }

            double absL = Math.abs(left);
            double absR = Math.abs(right);

            // modules close to each other
            double maxAbs = Math.max(absL, absR);
            boolean modulusClose = Math.abs(absL - absR) < 0.15 * maxAbs;

            // opposite signs
            boolean oppositeSigns = left * right < 0.0;

            if (modulusClose && oppositeSigns) {
                amount++;
            }
        }

        // if pass - symmetric interval
        return amount >= 3;
    }

    // ===IMPROPER INTEGRAL ====

    private static double safeTestIntegrate(Func1D f, double start, double end) {
        if (start >= end) return 0.0;
        try {
            // test
            double res = MathematicsLab3.trapezoid(f, start, end, 512);
            if (Double.isNaN(res) || Double.isInfinite(res)) {
                return 1e15;
            }
            return Math.abs(res);
        } catch (Exception e) {
            return 1e15;
        }
    }

    private static class ImproperCheckResult {
        final boolean convergent; // сходится ли интеграл
        final Double singularPoint; // точка разрыва: a, b, или null

        ImproperCheckResult(boolean convergent, Double singularPoint) {
            this.convergent = convergent;
            this.singularPoint = singularPoint;
        }
    }

    private static ImproperCheckResult checkImproperIntegral(Func1D f, double a, double b) {
        final double sigma = 1e-9;

        // проверка a
        double testY = f.safeApply(a + 1e-7);
        if (Double.isNaN(testY) || Double.isInfinite(testY) || Math.abs(testY) > 10.0) {
            // Вычисляем площадь "далеко" и "близко" к разрыву
            double areaFar = safeTestIntegrate(f, a + 0.001, a + 0.1);
            double areaNear = safeTestIntegrate(f, a + sigma, a + 0.001);

            // Если обе площади конечны и "близкая" не слишком больше "далёкой"
            if (Double.isFinite(areaFar) && Double.isFinite(areaNear)) {
                if (areaNear < areaFar * 20.0) {
                    return new ImproperCheckResult(true, a);
                }
            }
        }

        // проверка b
        double testYb = f.safeApply(b - 1e-7);
        if (Double.isNaN(testYb) || Double.isInfinite(testYb) || Math.abs(testYb) > 10.0) {
            double areaFar = safeTestIntegrate(f, b - 0.1, b - 0.001);
            double areaNear = safeTestIntegrate(f, b - 0.001, b - sigma);

            if (Double.isFinite(areaFar) && Double.isFinite(areaNear)) {
                if (areaNear < areaFar * 20.0) {
                    return new ImproperCheckResult(true, b);
                }
            }
        }

        // Нет сходящегося несобственного интеграла на границах
        return new ImproperCheckResult(false, null);
    }



    @Override
    public ApplicationStatus showUsage(ParsedString parsedString) {

        ClientConsole.println("integral <method>\n");

        ClientConsole.print("Avalilable methods: ");

        for (String command : commandSet) {
            ClientConsole.print(command + " ");
        }

        ClientConsole.print("Avalilable functions: \n");

        for (String command : functionsSet) {
            ClientConsole.println(command);
        }

        ClientConsole.println("\n");

        return ApplicationStatus.RUNNING;
    }
}

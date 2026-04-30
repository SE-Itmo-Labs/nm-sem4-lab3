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

            ClientConsole.log(form);

            return integrate(command, function, form.getXStart(), form.getXEnd(), form.getEpsilon());

        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
            ClientConsole.error(ex.toString());
        }

        return ApplicationStatus.RUNNING;
    }

    protected static ApplicationStatus integrate(String method, Func1D function, Double xStart, Double xEnd, Double epsilon) {

        if ((xStart * 1000) % 1.0 != 0.0 || (xEnd * 1000) % 1.0 != 0.0 || xStart > 1000 || xEnd > 1000 || xStart < -1000 ||  xEnd < -1000) {
            ClientConsole.error("Сделайте xStart и xEnd от -1000 до 1000 и 3 знака после запятой");
            return ApplicationStatus.RUNNING;
        }

        if (xStart >= xEnd) {
            ClientConsole.error("Недопустимые границы, левая больше или равна правой " + xStart + " " + xEnd);
            return ApplicationStatus.RUNNING;
        }

        if (epsilon >= 1 || epsilon < 0.001 || (epsilon * 1000) % 1.0 != 0.0) {
            ClientConsole.error("Сделайте epsilon от 0.001 до 0.999 и 3 знака после запятой");
            return ApplicationStatus.RUNNING;
        }

        if (Math.abs(xEnd - xStart) >= 1000) {
            ClientConsole.error("Я конечно могу ошибаться, но мне страшно на таких значениях запускать эту программу. Сделайте меньше delta x: " + xStart + ", " + xEnd);
            return ApplicationStatus.RUNNING;
        }

        if (!definedArea(function, xStart, xEnd)) {
            ClientConsole.error("В слишком многих промежутках функция не определена");
            return ApplicationStatus.RUNNING;
        }




        List<Double> weirdDots = weirdDots(function, xStart, xEnd);
        List<Double> activePoles = new ArrayList<>();

        for (Double pole : weirdDots) {
            if (pole >= xStart - 1e-7 && pole <= xEnd + 1e-7) {
                activePoles.add(pole);
            }
        }




        if (activePoles.isEmpty()) {
            IntegrationResult result = integrateRungeSafe(method, function, xStart, xEnd, epsilon);
            ClientConsole.log("Результат: " + result.value);
            ClientConsole.log("Погрешность: " + String.format("%.2e", result.error));
            ClientConsole.log("Шаги: " + result.n);
            ClientConsole.log("Статус: " + result.message);
            return ApplicationStatus.RUNNING;
        }

        ClientConsole.log("Найдены особенности: " + activePoles.size() + " (" + weirdDots.size() + ") ");




        List<String> messages = new ArrayList<>();
        List<double[]> currentIntervals = new ArrayList<>();
        currentIntervals.add(new double[]{xStart, xEnd});

        for (Double pole : activePoles) {
            boolean isAtA = Math.abs(pole - xStart) < 1e-6;
            boolean isAtB = Math.abs(pole - xEnd) < 1e-6;

            // приколы на границах
            if (isAtA || isAtB) {
                ImproperCheckResult improper = checkImproperIntegral(function, xStart, xEnd);
                if (improper.convergent) {
                    IntegrationResult result = integrateImproper(method, function, xStart, xEnd, pole, epsilon);
                    ClientConsole.log("Результат: " + result.value);
                    ClientConsole.log(result.message);
                    return ApplicationStatus.RUNNING;
                }
                ClientConsole.error("Интеграл не существует (расходится в граничной точке " +
                        String.format("%.4f", pole) + ")");
                return ApplicationStatus.RUNNING;
            }

            // internal pole (xStart < pole < xEnd)
            if (isCauchyPrincipalValuePoint(function, pole)) {


                // Search for interval containing pole
                for (int idx = 0; idx < currentIntervals.size(); idx++) {
                    double[] interval = currentIntervals.get(idx);
                    Double intA = interval[0], intB = interval[1];

                    if (pole > intA - 1e-6 && pole < intB + 1e-6) {
                        Double leftDist = pole - intA;
                        Double rightDist = intB - pole;
                        Double d = Math.min(leftDist, rightDist);

                        Double zeroedStart = pole - d;
                        Double zeroedEnd = pole + d;

                        currentIntervals.remove(idx);
                        messages.add("Главное значение: занулен участок [" +
                                String.format("%.3f", zeroedStart) + ", " +
                                String.format("%.3f", zeroedEnd) + "] вокруг " +
                                String.format("%.3f", pole));

                        if (leftDist > rightDist + 1e-6) {
                            currentIntervals.add(idx, new double[]{intA, zeroedStart});
                        }
                        if (rightDist > leftDist + 1e-6) {
                            currentIntervals.add(idx, new double[]{zeroedEnd, intB});
                        }
                        break;
                    }
                }
            } else {
                ClientConsole.error("Интеграл не существует (неинтегрируемый разрыв 2-го рода в точке " +
                        String.format("%.4f", pole) + ")");
                return ApplicationStatus.RUNNING;
            }
        }

        // Sum integrals of remaining intervals
        Double totalValue = 0.0;
        int totalN = 0;
        Double maxError = 0.0;

        for (double[] interval : currentIntervals) {
            Double start = interval[0], end = interval[1];
            if (end - start < 1e-8) continue;

            IntegrationResult ans = integrateRungeSafe(method, function, start, end, epsilon);

            if (Double.isNaN(ans.value) || Double.isInfinite(ans.value) || ans.error > epsilon * 100) {
                ClientConsole.error("Интеграл не существует (расходится на участке [" +
                        String.format("%.3f", start) + ", " + String.format("%.3f", end) + "])");
                return ApplicationStatus.RUNNING;
            }

            totalValue += ans.value;
            totalN += ans.n;
            maxError = Math.max(maxError, ans.error);
        }


        String finalMsg = messages.isEmpty() ? "Успешно" : String.join(";\n", messages);
        ClientConsole.log("Результат: " + totalValue);
        ClientConsole.log("Погрешность: " + String.format("%.2e", maxError));
        ClientConsole.log("Сообщение: " + finalMsg);

        return ApplicationStatus.RUNNING;
    }

    public static boolean definedArea(Func1D f, Double a, Double b) {

        int steps = 400;
        Double dx = (b - a) / steps;
        int badCount = 0;

        for (int i = 0; i <= steps; i++) {

            Double x = a + i * dx;
            Double y = f.safeApply(x);
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                badCount++;
            }

        }

        Double badRatio = (double) badCount / steps;

        ClientConsole.log("definedArea : " + (1 - badRatio) * 100 + "% (" + badRatio*100 + "%)");

        return badRatio <= 0.35;
    }

    private static Double refinePole(Func1D f, Double left, Double right) {
        Double l = Math.max(left, left); // coerceAtLeast
        Double r = right;

        if (r - l < 1e-14) {
            return (l + r) / 2.0;
        }

        Double bestPos = (l + r) / 2.0;
        Double bestAbs = 0.0;

        for (int iter = 0; iter < 200; iter++) {
            Double m1 = l + (r - l) / 3.0;
            Double m2 = r - (r - l) / 3.0;

            Double v1 = f.apply(m1);
            Double v2 = f.apply(m2);

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
            Double m = (l + r) / 2.0;
            Double vm = f.apply(m);

            if (vm > bestAbs) {
                bestAbs = vm;
                bestPos = m;
            }

            Double leftTest = f.apply(m - (r - l) * 0.1);
            Double rightTest = f.apply(m + (r - l) * 0.1);

            if (leftTest > rightTest) {
                r = m;
            } else {
                l = m;
            }
        }

        return bestPos;
    }

    public static List<Double> weirdDots(Func1D f, Double a, Double b) {
        List<Double> singularities = new ArrayList<>();
        int steps = 50_000;
        Double dx = (b - a) / steps;
        Double radius = 0.0001;

        Double prevX = a;
        Double prevY = f.apply(a);

        for (int i = 0; i <= steps; i++) {
            Double x = a + i * dx;
            Double y = f.apply(x);

            boolean foundPotentialPole = clarifyWeirdDot(y, prevY);

            if (foundPotentialPole) {

                // Clarify the coordinate in [prevX-2dx, x+2dx]

                Double searchLeft = Math.max(prevX - dx * 2, a);
                Double searchRight = Math.min(x + dx * 2, b);

                Double refined = refinePole(f, searchLeft, searchRight);

                Double rounded = Math.round(refined * 1e8) / 1e8;

                if (Math.abs(rounded) < 1e-9)
                    rounded = 0.0;

                boolean alreadyExists = false;

                for (Double existing : singularities) {
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

    private static boolean clarifyWeirdDot(Double y, Double prevY) {
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

    private static boolean isCauchyPrincipalValuePoint(Func1D f, Double s) {
        int amount = 0;
        // Тестовые расстояния от точки разрыва
        double[] testPoints = {0.1, 0.05, 0.02, 0.01, 0.005};

        for (Double h : testPoints) {

            Double left = f.safeApply(s - h);
            Double right = f.safeApply(s + h);

            // skip if one of values is invalid
            if (!Double.isFinite(left) || !Double.isFinite(right) ||
                    Double.isNaN(left) || Double.isNaN(right)) {
                continue;
            }

            Double absL = Math.abs(left);
            Double absR = Math.abs(right);

            // modules close to each other
            Double maxAbs = Math.max(absL, absR);
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

    private static Double safeTestIntegrate(Func1D f, Double start, Double end) {
        if (start >= end) return 0.0;
        try {
            // test
            Double res = MathematicsLab3.trapezoid(f, start, end, 512);
            if (Double.isNaN(res) || Double.isInfinite(res)) {
                return 1e15;
            }
            return Math.abs(res);
        } catch (Exception e) {
            return 1e15;
        }
    }

    private static class ImproperCheckResult {
        final boolean convergent;
        final Double singularPoint; // singular at: a, b, or null

        ImproperCheckResult(boolean convergent, Double singularPoint) {
            this.convergent = convergent;
            this.singularPoint = singularPoint;
        }
    }

    private static ImproperCheckResult checkImproperIntegral(Func1D f, Double a, Double b) {
        final Double sigma = 1e-9;

        // check a
        Double testY = f.safeApply(a + 1e-7);
        if (Double.isNaN(testY) || Double.isInfinite(testY) || Math.abs(testY) > 10.0) {
            // S "far" and "near" are close to singularity

            Double areaFar = safeTestIntegrate(f, a + 0.001, a + 0.1);
            Double areaNear = safeTestIntegrate(f, a + sigma, a + 0.001);

            // If two S are finite (limited) and near one is not greater than far one
            if (Double.isFinite(areaFar) && Double.isFinite(areaNear)) {
                if (areaNear < areaFar * 20.0) {
                    return new ImproperCheckResult(true, a);
                }
            }
        }

        // check b
        Double testYb = f.safeApply(b - 1e-7);
        if (Double.isNaN(testYb) || Double.isInfinite(testYb) || Math.abs(testYb) > 10.0) {
            Double areaFar = safeTestIntegrate(f, b - 0.1, b - 0.001);
            Double areaNear = safeTestIntegrate(f, b - 0.001, b - sigma);

            if (Double.isFinite(areaFar) && Double.isFinite(areaNear)) {
                if (areaNear < areaFar * 20.0) {
                    return new ImproperCheckResult(true, b);
                }
            }
        }

        return new ImproperCheckResult(false, null);
    }

    private static int getMethodOrder(String method) {
        return switch (method.toLowerCase()) {
            case "leftrectangles", "rightrectangles", "middlerectangles" -> 2; // прямоугольники: ошибка ~1/n²
            case "trapezoid" -> 2;  // trapezoid: ~1/n^2
            case "simpsons" -> 4;   // simpson ~1/n^4
            default -> 2;           // 2
        };
    }

    private static Double calculateByMethodName(String method, Func1D f, Double a, Double b, int n) {
        return switch (method.toLowerCase()) {
            case "leftrectangles" -> MathematicsLab3.leftRectangles(f, a, b, n);
            case "rightrectangles" -> MathematicsLab3.rightRectangles(f, a, b, n);
            case "middlerectangles" -> MathematicsLab3.middleRectangles(f, a, b, n);
            case "simpsons" -> MathematicsLab3.simpsons(f, a, b, n);
            default -> MathematicsLab3.trapezoid(f, a, b, n);
        };
    }

    private static class IntegrationResult {
        final Double value;   // значение интеграла
        final int n;          // использованное количество шагов
        final Double error;   // оценённая погрешность
        final String message; // статус: "Успешно" или предупреждение

        IntegrationResult(Double value, int n, Double error, String message) {
            this.value = value;
            this.n = n;
            this.error = error;
            this.message = message;
        }
    }

    private static IntegrationResult integrateRungeSafe(String method, Func1D f, Double a, Double b, Double eps) {

        int n = 4;  // start interval amount
        Double i0 = calculateByMethodName(method, f, a, b, n / 2);
        Double i1;
        int k = getMethodOrder(method);
        Double error;
        final int MAX_N = 2_097_152;

        do {
            i1 = calculateByMethodName(method, f, a, b, n);

            // invalid values check
            if (Double.isNaN(i1) || Double.isInfinite(i1)) {
                return new IntegrationResult(Double.NaN, n, Double.POSITIVE_INFINITY,
                        "Ошибка: получено бесконечное значение");
            }

            // Runge formula
            Double denominator = Math.pow(2.0, k) - 1.0;
            error = Math.abs(i1 - i0) / denominator;

            i0 = i1;
            n *= 2;  // double interval amount

        } while (error > eps && n <= MAX_N);

        String status = (error > eps)
                ? "Предупреждение: точность не достигнута (погрешность=" + String.format("%.2e", error) + ")"
                : "Успешно";

        return new IntegrationResult(i1, n / 2, error, status);
    }

    private static IntegrationResult integrateImproper(String method, Func1D f, Double a, Double b,
                                                       Double pole, Double eps) {
        final Double sigma = 1e-10;  // min offset

        // move border if pole is the same
        Double newA = (Math.abs(pole - a) < 1e-6) ? a + sigma : a;
        Double newB = (Math.abs(pole - b) < 1e-6) ? b - sigma : b;

        IntegrationResult result = integrateRungeSafe(method, f, newA, newB, eps);

        return new IntegrationResult(
                result.value,
                result.n,
                result.error,
                "Сходящийся несобственный интеграл (вычислен с отступом σ=" + sigma + ")"
        );
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

package com.ssnagin.collectionmanager.commands.commands;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.math.lab3.Lab3Form;
import com.ssnagin.collectionmanager.math.lab3.MathematicsLab3;
import com.ssnagin.collectionmanager.reflection.Reflections;
import com.ssnagin.collectionmanager.scripts.ScriptManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

        try {
            Lab3Form form = Reflections.parseModel(Lab3Form.class, scanner);

            switch (command) {
                case "leftRectangles":
                    result = MathematicsLab3.leftRectangles(form.getXStart(), form.getXEnd(), form.getK());
                    break;
            }


        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
            ClientConsole.error(ex.toString());
        }

        return ApplicationStatus.RUNNING;
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

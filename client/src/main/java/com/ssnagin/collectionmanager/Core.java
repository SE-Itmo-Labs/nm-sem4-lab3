package com.ssnagin.collectionmanager;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.Command;
import com.ssnagin.collectionmanager.commands.CommandManager;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.commands.commands.*;
import com.ssnagin.collectionmanager.commands.commands.legacy.CommandShowGUI;
import com.ssnagin.collectionmanager.config.Config;
import com.ssnagin.collectionmanager.console.Console;
import com.ssnagin.collectionmanager.gui.ToolkitInitializer;
import com.ssnagin.collectionmanager.gui.window.Window;
import com.ssnagin.collectionmanager.gui.window.WindowManager;
import com.ssnagin.collectionmanager.gui.window.WindowParameters;
import com.ssnagin.collectionmanager.inputparser.InputParser;
import com.ssnagin.collectionmanager.inputparser.ParseMode;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.scripts.ScriptManager;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.NoSuchElementException;

/**
 * @author developer
 */
@ToString
public class Core extends AbstractCore {

    @Getter
    protected static Core instance = new Core();

    @Getter
    protected ApplicationStatus applicationStatus;

    protected ScriptManager scriptManager;

//    protected ClientGUI clientGUI;

    protected WindowManager windowManager;

    public static final String LOGO = String.format("                                                                                             \n" + "                                                                                             \n" +
            "▄▄ ▄▄ ▄▄  ▄▄▄▄ ▄▄ ▄▄ ▄▄   ▄▄  ▄▄▄ ▄▄▄▄▄▄      ▄▄ ▄▄ ▄▄   ▄▄▄▄▄▄ ▄▄ ▄▄   ▄▄  ▄▄▄ ▄▄▄▄▄▄ ▄▄▄▄▄ \n" +
            "██▄██ ██ ██▀▀▀ ██▄██ ██▀▄▀██ ██▀██  ██        ██ ██ ██     ██   ██ ██▀▄▀██ ██▀██  ██   ██▄▄  \n" +
            " ▀█▀  ██ ▀████ ██ ██ ██   ██ ██▀██  ██  ▄▄▄▄▄ ▀███▀ ██▄▄▄  ██   ██ ██   ██ ██▀██  ██   ██▄▄▄ \n" +
            "                                                                                             \n" +
            "  ver. %s | github.com/SE-Itmo-Labs/nm-sem4-lab3.git                          \n\n", Config.Core.VERSION);


    @SneakyThrows
    public Core() {
        super();

        // Init JAVAFX toolkit
        ToolkitInitializer.start();

        // Singletone pattern
        this.commandManager = CommandManager.getInstance();
        this.scriptManager = ScriptManager.getInstance();
        this.windowManager = WindowManager.getInstance();

        this.networking = new Networking("localhost", Config.Networking.PORT);

        // init CLI commands
        registerCommands();

        // init GUI Windows
        registerGUIWindows();

        this.setApplicationStatus(ApplicationStatus.RUNNING);
    }

    private void registerCommands() {

        this.commandManager.register(new CommandExit("exit", "exit this useless piece of masterpiece"));
        this.commandManager.register(new CommandHelp("help", "display help on available commands", commandManager));

        this.commandManager.register(new CommandIntegral("integral", "use rectangles method", scriptManager));

        //this.commandManager.register(new CommandExecuteScript("execute_script", "some description here", commandManager, scriptManager));
        //this.commandManager.register(new CommandAdd("add", "add an object to collection", networking, scriptManager));
        //this.commandManager.register(new CommandShow("show", "show collection's elements", networking));
        //this.commandManager.register(new CommandClear("clear", "clear collection elements", networking));
        //this.commandManager.register(new CommandUpdate("update", "update <id> | update values of selected collection by id", networking, scriptManager));
        //this.commandManager.register(new CommandRemoveById("remove_by_id", "remove_by_id <id> | removes an element with selected id", networking));
        //this.commandManager.register(new CommandAddIfMin("add_if_min", "adds an element into collection if it is the lowest element in it", scriptManager, networking));
        this.commandManager.register(new CommandHistory("history", "shows last 9 executed commands", commandManager));
        //this.commandManager.register(new CommandCountByNumberOfParticipants("count_by_number_of_participants", "count_by_number_of_participants <numberOfParticipants>| shows the amount of fields with the same amount of participants", networking));
        //this.commandManager.register(new CommandRandom("random", "random <amount> | adds to collection <amount> random elements", networking));

        //this.commandManager.register(new CommandLogin("login", "Log in into the system", networking, scriptManager));
        //this.commandManager.register(new CommandLogout("logout", "Log out from the system", networking));
        //this.commandManager.register(new CommandRegister("register", "Register in the system", networking, scriptManager));

        this.commandManager.register(new CommandShowGUI("gui", "show / hide gui", windowManager));
    }

    private void registerGUIWindows() {
        this.windowManager.register(new Window(
                "main",
                new WindowParameters(
                    true,
                    1080,
                    720,
                    "Collection Manager ver. 1.3",
                        "/com/ssnagin/collectionmanager/fxml/main.fxml",
                        "/com/ssnagin/collectionmanager/css/style.css"
                ),
                true
        ));
        this.windowManager.register(new Window(
                "auth",
                new WindowParameters(
                        true,
                        500,
                        720,
                        "Auth page",
                        "/com/ssnagin/collectionmanager/fxml/subwindows/auth.fxml",
                        "/com/ssnagin/collectionmanager/css/animation.css"
                )
        ));
        this.windowManager.register(new Window(
                "add",
                new WindowParameters(
                    true,
                    420,
                    610,
                    "Add page",
                    "/com/ssnagin/collectionmanager/fxml/subwindows/add.fxml",
                    "/com/ssnagin/collectionmanager/css/style.css"
                )
        ));
        this.windowManager.register(new Window(
                "animation",
                new WindowParameters(
                        true,
                        1080,
                        720,
                        "Animation",
                        "/com/ssnagin/collectionmanager/fxml/subwindows/animation.fxml",
                        "/com/ssnagin/collectionmanager/css/animation.css"
                )
        ));
    }

    @Override
    @SneakyThrows
    public void start() {
        super.start();

        // Step-by-step description of the algorithm.

        // 0. First, print logo

        this.printLogo();

        // 2. Wait for the user input.
        // After it, parse given arguments with ArgumentParser

        ParsedString parsedString;
        String inputLine = "";
        while (true) {

            Console.print(Console.getShellArrow());

            // I need to replace this code for the future custom input (executeCommand from script) integration.
//
//            String inputLine = this.scriptManager.getCurrentScanner().hasNextLine()
//                    ? this.scriptManager.getCurrentScanner().nextLine()
//                    : "";

            try {
                inputLine = this.scriptManager.getCurrentScanner().hasNextLine()
                        ? this.scriptManager.getCurrentScanner().nextLine() : null;
                if (inputLine == null) onExit();
            } catch (NoSuchElementException e) {
                onExit();
            }

            if (inputLine.contains("\u0004")) {
                onExit();
            }

            parsedString = InputParser.parse(inputLine, ParseMode.COMMAND_ONLY);

            // 2.1 If the string is null, skip the code:

            if (parsedString.isEmpty()) {
                // ... some code
                continue;
            }

            // 3 Executing commands according to user's input
            this.runCommand(parsedString);
        }
    }

    public void printLogo() {
        Console.print(Core.LOGO);
    }

    protected void runCommand(ParsedString parsedString) {
        Command command = this.commandManager.get(parsedString.getCommand());
        if (command instanceof UserCommand)
            this.setApplicationStatus(
                    ((UserCommand) command).executeCommand(parsedString)
            );
        else
            this.setApplicationStatus(
                    new CommandDefault("", "").executeCommand(parsedString)
            );
    }

    private void setApplicationStatus(ApplicationStatus applicationStatus) {

        this.applicationStatus = applicationStatus;

        if (applicationStatus != ApplicationStatus.RUNNING) {
            this.onExit();
        }
    }

    // === EVENTS ==== //

    @Override
    public void onExit() {
        // Some code here, for example saving json.

        Console.separatePrint("Bye, have a great time!", this.getApplicationStatus().toString());
        System.exit(this.getApplicationStatus().getCode());
    }

}
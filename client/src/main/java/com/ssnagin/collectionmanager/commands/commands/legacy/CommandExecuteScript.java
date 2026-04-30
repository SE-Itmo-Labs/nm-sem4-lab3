/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.CommandManager;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.inputparser.InputParser;
import com.ssnagin.collectionmanager.inputparser.ParseMode;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.scripts.ScriptManager;
import com.ssnagin.collectionmanager.scripts.exceptions.ScriptRecursionException;

import java.io.File;
import java.io.IOException;

/**
 * Shows brief description about available commands
 *
 * @author developer
 */
public class CommandExecuteScript extends UserCommand {

    private final CommandManager commandManager;
    private ScriptManager scriptManager;

    public CommandExecuteScript(String name, String description, CommandManager commandManager, ScriptManager scriptManager) {
        super(name, description);
        this.commandManager = commandManager;
        this.scriptManager = scriptManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        // FOR THE FUTURE:
        // This command requires uri string, so we will have to edit ParsedString to ParseMode.URI !!!

        parsedString = InputParser.parse(parsedString.getRowArguments(), ParseMode.COMMAND_ONLY);

        File file = new File(parsedString.getCommand());

        try {
            this.scriptManager.pushFileScanner(file);

            while (this.scriptManager.getCurrentScanner().hasNextLine()) {
                String line = this.scriptManager.getCurrentScanner().nextLine().trim();
                if (line.isEmpty()) continue;

                ParsedString scriptCommand = InputParser.parse(line, ParseMode.COMMAND_ONLY);
                UserCommand command = (UserCommand) commandManager.get(scriptCommand.getCommand());

                ApplicationStatus status = command.executeCommand(scriptCommand);

                if (status != ApplicationStatus.RUNNING) return status;
            }
        } catch (IOException e) {
            ClientConsole.error("Script file not found: " + e.getMessage());

        } catch (ScriptRecursionException e) {
            ClientConsole.error("Recursion detected! " + e.getMessage());
            this.scriptManager.clearActiveScripts();
            this.scriptManager.removeScanners();
            return ApplicationStatus.RUNNING;
        } catch (Exception e) {
            ClientConsole.error("Un");
        }
        //finally {
        try {
            this.scriptManager.popScanner(file);
        } catch (IOException e) {
            ClientConsole.error("Error while accessing to File, stop all executables...");
        }
        //}

        return ApplicationStatus.RUNNING;
    }
}

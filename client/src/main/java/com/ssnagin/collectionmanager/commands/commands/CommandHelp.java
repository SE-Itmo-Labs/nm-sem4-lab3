/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.commands.commands;

import com.ssnagin.collectionmanager.Client;
import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.CommandManager;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.console.Console;
import com.ssnagin.collectionmanager.inputparser.ParsedString;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Shows brief description about available commands
 *
 * @author developer
 */
public class CommandHelp extends UserCommand {

    protected CommandManager commandManager;

    private String temporaryCreatedHeadMessage = "VICHMAT ULTIMATE is a nice tool though.\nHere are available commands:\n";

    public CommandHelp(String name, String description, CommandManager commandManager) {
        super(name, description);

        this.commandManager = commandManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        ClientConsole.println(temporaryCreatedHeadMessage);

        ArrayList<String> sortedKeys = new ArrayList<>(this.commandManager.getCommands().keySet());
        Collections.sort(sortedKeys);

        UserCommand selectedCommand;

        for (String command : sortedKeys) {

            selectedCommand = (UserCommand) this.commandManager.get(command);

            ClientConsole.println(selectedCommand.getName() + "   " + selectedCommand.getDescription());
        }

        return ApplicationStatus.RUNNING;
    }
}

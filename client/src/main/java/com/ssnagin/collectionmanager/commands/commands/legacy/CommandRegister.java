package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.events.EventType;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.reflection.Reflections;
import com.ssnagin.collectionmanager.scripts.ScriptManager;
import com.ssnagin.collectionmanager.user.objects.InternalUser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class CommandRegister extends UserNetworkCommand {

    private ScriptManager scriptManager;

    public CommandRegister(String name, String description, Networking networking, ScriptManager scriptManager) {
        super(name, description, networking);

        this.scriptManager = scriptManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Scanner scanner = this.scriptManager.getCurrentScanner();

        ClientConsole.separatePrint("Please, fill in registration form:", this.getName().toUpperCase());

        try {

            InternalUser user = Reflections.parseModel(InternalUser.class, scanner);
            user.setIsBanned(0);

            ServerResponse response = this.networking.sendClientRequest(
                    new ClientRequest(parsedString, user)
            );
            ClientConsole.separatePrint(response.getMessage(), "SERVER");

            eventManager.publish(EventType.USER_REGISTERED.toString(), user);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
            ClientConsole.error(ex.toString());
        }
        
        return ApplicationStatus.RUNNING;
    }
}

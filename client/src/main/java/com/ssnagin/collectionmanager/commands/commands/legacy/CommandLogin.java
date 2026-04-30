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
import com.ssnagin.collectionmanager.session.SessionKey;
import com.ssnagin.collectionmanager.user.objects.InternalUser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class CommandLogin extends UserNetworkCommand {

    protected ScriptManager scriptManager;

    public CommandLogin(String name, String description, Networking networking, ScriptManager scriptManager) {
        super(name, description, networking);

        this.scriptManager = scriptManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Scanner scanner = this.scriptManager.getCurrentScanner();

        try {
            InternalUser user = Reflections.parseModel(InternalUser.class, scanner);
            user.setIsBanned(0);

            ClientRequest clientRequest = new ClientRequest(
                parsedString, user
            );

            ServerResponse serverResponse = this.networking.sendClientRequest(clientRequest);

            SessionKey sessionKey = (SessionKey) serverResponse.getData();

            sessionKeyManager.setSessionKey(sessionKey);
            
            ClientConsole.separatePrint(
                    serverResponse.getMessage(),
                    String.valueOf(serverResponse.getResponseStatus())
            );

            eventManager.publish(EventType.USER_LOGGED_IN.toString(), user);

        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            ClientConsole.error(e);
        }

        return ApplicationStatus.RUNNING;
    }
}


package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.events.EventType;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;

public class CommandLogout extends UserNetworkCommand {

    private final String TEXT_PLACEHOLDER = "Successfully logged out";

    public CommandLogout(String name, String description, Networking networking) {
        super(name, description, networking);
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;
        // Доработать и сделать logout на сервере
        sessionKeyManager.setSessionKey(null);

        eventManager.publish(EventType.USER_LOGGED_OUT.toString(), null);
        ClientConsole.log("Logged out");

        return ApplicationStatus.RUNNING;
    }
}
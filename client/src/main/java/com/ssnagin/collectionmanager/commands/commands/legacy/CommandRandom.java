package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.events.EventType;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.networking.wrappers.SessionClientRequest;

import java.io.IOException;

public class CommandRandom extends UserNetworkCommand {


    public CommandRandom(String name, String description, Networking networking) {
        super(name, description, networking);
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        try {
            long id;
            // Try to parse Integer
            id = Long.parseLong(parsedString.getArguments().get(0));

            ServerResponse response = this.networking.sendClientRequest(
                    new SessionClientRequest(new ClientRequest(parsedString, id), sessionKeyManager.getSessionKey())
            );

            ClientConsole.separatePrint(response.getResponseStatus(), "SERVER");
            ClientConsole.separatePrint(response.getMessage(), "SERVER");

            eventManager.publish(EventType.COLLECTION_DATA_CHANGED.toString(), null);
        } catch (NumberFormatException ex) {
            ClientConsole.log("Wrong number format");
            return ApplicationStatus.RUNNING;
        } catch (IndexOutOfBoundsException ex) {
            return this.showUsage(parsedString);
        } catch (IOException | ClassNotFoundException e) {
            ClientConsole.error(e.toString());
        }

        return ApplicationStatus.RUNNING;
    }
}

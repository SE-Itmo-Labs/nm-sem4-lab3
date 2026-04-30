package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.networking.wrappers.SessionClientRequest;
import com.ssnagin.collectionmanager.reflection.Reflections;

import java.io.IOException;
import java.util.NoSuchElementException;

public class CommandCountByNumberOfParticipants extends UserNetworkCommand {

    public CommandCountByNumberOfParticipants(String name, String description, Networking networking) {
        super(name, description, networking);
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Long numberOfParticipants;

        try {
            numberOfParticipants = (Long) Reflections.parsePrimitiveInput(
                    Long.class,
                    parsedString.getArguments().get(0)
            );

            ServerResponse serverResponse = this.networking.sendClientRequest(
                    new SessionClientRequest(
                            new ClientRequest(
                                    parsedString,
                                    numberOfParticipants
                            ),
                            sessionKeyManager.getSessionKey()
                    )
            );

            ClientConsole.separatePrint(serverResponse.getMessage(), "SERVER");

        } catch (NumberFormatException ex) {
            ClientConsole.log("Неверный формат числа");
            return ApplicationStatus.RUNNING;
        } catch (IndexOutOfBoundsException | NoSuchElementException | IOException | ClassNotFoundException ex) {
            return this.showUsage(parsedString);
        }



        return ApplicationStatus.RUNNING;
    }
}
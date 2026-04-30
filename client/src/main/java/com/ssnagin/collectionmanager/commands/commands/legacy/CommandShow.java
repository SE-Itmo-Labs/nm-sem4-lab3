/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.collection.model.MusicBand;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.networking.ResponseStatus;
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.networking.wrappers.SessionClientRequest;
import com.ssnagin.collectionmanager.reflection.Reflections;

import java.io.IOException;

/**
 * Shows brief description about available commands
 *
 * @author developer
 */
public class CommandShow extends UserNetworkCommand {

    public CommandShow(String name, String description, Networking networking) {
        super(name, description, networking);
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        ServerResponse response;

        // Trying to parse positive int value:
        Long page;

        try {
            page = (Long) Reflections.parsePrimitiveInput(
                    Long.class,
                    parsedString.getArguments().get(0)
            );

            if (page <= 0) throw new NumberFormatException();

        } catch (NumberFormatException ex) {
            ClientConsole.error("Неверный формат числа");
            return ApplicationStatus.RUNNING;
        } catch (IndexOutOfBoundsException e) {
            page = 1L;
        }

        try {

            ClientRequest clientRequest = new SessionClientRequest(
                    new ClientRequest(parsedString, page, 1),
                    sessionKeyManager.getSessionKey()
            );

            response = this.networking.sendClientRequest(clientRequest);
            ClientConsole.separatePrint(response.getMessage(), "SERVER");

            clientRequest.setStage(100);
            do {
                response = this.networking.sendClientRequest(clientRequest);
                clientRequest.setStage(response.getStage());

                if (response.getResponseStatus() != ResponseStatus.OK) {
                    ClientConsole.separatePrint(response.getMessage(), "SERVER");
                    break;
                }

                if (response.getData() == null) break;
                if (!(response.getData() instanceof MusicBand)) break;

                var data = (MusicBand) response.getData();

                ClientConsole.println(
                        data.getDescription()
                );

            } while (response.getStage() != 99 || response.getResponseStatus() == ResponseStatus.OK);

        } catch (IOException | ClassNotFoundException e) {
            ClientConsole.error(e.toString());
        }

        return ApplicationStatus.RUNNING;
    }
}

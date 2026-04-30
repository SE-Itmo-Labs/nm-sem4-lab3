/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.collection.model.MusicBand;
import com.ssnagin.collectionmanager.collection.wrappers.LocalDateWrapper;
import com.ssnagin.collectionmanager.commands.UserNetworkCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.description.DescriptionParser;
import com.ssnagin.collectionmanager.events.EventType;
import com.ssnagin.collectionmanager.inputparser.ParsedString;
import com.ssnagin.collectionmanager.networking.Networking;
import com.ssnagin.collectionmanager.networking.ResponseStatus;
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.networking.wrappers.SessionClientRequest;
import com.ssnagin.collectionmanager.reflection.Reflections;
import com.ssnagin.collectionmanager.scripts.ScriptManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author developer
 */
public class CommandUpdate extends UserNetworkCommand {

    private ScriptManager scriptManager;

    public CommandUpdate(String name,
                         String description, Networking networking, ScriptManager scriptManager) {
        super(name, description, networking);

        this.scriptManager = scriptManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Scanner scanner = this.scriptManager.getCurrentScanner();

        Long id;
        Integer stage;

        try {
            id = (Long) Reflections.parsePrimitiveInput(
                    Long.class,
                    parsedString.getArguments().get(0)
            );
        } catch (NumberFormatException ex) {
            ClientConsole.log("Неверный формат числа");
            return ApplicationStatus.RUNNING;
        } catch (IndexOutOfBoundsException e) {
            return showUsage(parsedString);
        }


        try {
            ServerResponse serverResponse = this.networking.sendClientRequest(

                    new SessionClientRequest(
                            new ClientRequest(
                                    parsedString,
                                    id,
                                    1
                            ),
                            sessionKeyManager.getSessionKey()
                    )
            );

            if (serverResponse.getResponseStatus() != ResponseStatus.OK) {
                ClientConsole.separatePrint(
                        serverResponse.getMessage(),
                        String.valueOf(serverResponse.getResponseStatus()
                        )
                );
                return ApplicationStatus.RUNNING;
            }

            stage = serverResponse.getStage();
            ClientConsole.separatePrint("Please, fill in the form with your values:", "SERVER");

            LocalDateWrapper result = new LocalDateWrapper(
                    Reflections.parseModel(MusicBand.class, scanner)
            );

            result.setId(id);

            serverResponse = this.networking.sendClientRequest(
                new SessionClientRequest(
                        new ClientRequest(
                                parsedString,
                                result,
                                stage
                        ),
                        sessionKeyManager.getSessionKey()
                )
            );

            ClientConsole.separatePrint(serverResponse.getMessage(), serverResponse.getResponseStatus().toString());

        } catch (IndexOutOfBoundsException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | IOException | ClassNotFoundException ex) {
            ClientConsole.error(ex.toString());
            return ApplicationStatus.RUNNING;
        }

        eventManager.publish(EventType.COLLECTION_DATA_CHANGED.toString(), null);

        return ApplicationStatus.RUNNING;
    }

    @Override
    public ApplicationStatus showUsage(ParsedString parsedString) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("usage: update <id> <field> <value>\n").append("things that can be updated:\n");
        stringBuilder.append(DescriptionParser.getRecursedDescription(MusicBand.class, new HashMap<>()));

        ClientConsole.println(stringBuilder);

        return ApplicationStatus.RUNNING;
    }
}
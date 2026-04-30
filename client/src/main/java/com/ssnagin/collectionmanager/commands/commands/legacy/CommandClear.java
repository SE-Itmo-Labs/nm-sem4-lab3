/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 * Throws when other commands does not exist. The only one unregistered command!
 *
 * @author developer
 */
public class CommandClear extends UserNetworkCommand {

    public CommandClear(String name, String description, Networking networking) {
        super(name, description, networking);
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        try {
            ServerResponse response = this.networking.sendClientRequest(
                    new SessionClientRequest(new ClientRequest(parsedString), sessionKeyManager.getSessionKey())
            );

            ClientConsole.separatePrint(response.getData() + " " + response.getMessage(), "SERVER");

            // Кидаем event на обновление данных таблицы
            eventManager.publish(EventType.COLLECTION_DATA_CHANGED.toString(), null);

        } catch (IOException | ClassNotFoundException e) {
            ClientConsole.error(e.toString());
        }

        return ApplicationStatus.RUNNING;
    }
}

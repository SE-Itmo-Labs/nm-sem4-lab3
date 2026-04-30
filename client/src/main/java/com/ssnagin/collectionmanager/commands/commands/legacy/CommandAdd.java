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
import com.ssnagin.collectionmanager.networking.data.client.ClientRequest;
import com.ssnagin.collectionmanager.networking.data.server.ServerResponse;
import com.ssnagin.collectionmanager.networking.wrappers.SessionClientRequest;
import com.ssnagin.collectionmanager.reflection.Reflections;
import com.ssnagin.collectionmanager.scripts.ScriptManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

public class CommandAdd extends UserNetworkCommand {

    private ScriptManager scriptManager;

    public CommandAdd(String name, String description, Networking networking, ScriptManager scriptManager) {
        super(name, description, networking);

        this.scriptManager = scriptManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        Scanner scanner = this.scriptManager.getCurrentScanner();

        ClientConsole.separatePrint("Please, fill in the form with your values:", this.getName().toUpperCase());

        try {

            MusicBand musicBand = Reflections.parseModel(MusicBand.class, scanner);
            var result = new LocalDateWrapper(musicBand);

            ServerResponse response = this.networking.sendClientRequest(
                    new SessionClientRequest(new ClientRequest(parsedString, result), this.sessionKeyManager.getSessionKey())
            );
            ClientConsole.separatePrint(response.getMessage(), "SERVER");

            // Кидаем event на обновление данных таблицы
            eventManager.publish(EventType.COLLECTION_DATA_CHANGED.toString(), null);

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException ex) {
            ClientConsole.error(ex.toString());
        }

        return ApplicationStatus.RUNNING;
    }

    @Override
    public ApplicationStatus showUsage(ParsedString parsedString) {
        ClientConsole.println("Usage: add\nСписок того, что надо ввести:");
        ClientConsole.println(DescriptionParser.getRecursedDescription(MusicBand.class, new HashMap<>()));

        return ApplicationStatus.RUNNING;
    }
}
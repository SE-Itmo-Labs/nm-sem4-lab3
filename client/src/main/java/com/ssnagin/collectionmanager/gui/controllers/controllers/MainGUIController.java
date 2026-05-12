package com.ssnagin.collectionmanager.gui.controllers.controllers;

import com.ssnagin.collectionmanager.collection.model.MusicBand;
import com.ssnagin.collectionmanager.commands.CommandManager;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.events.EventType;
import com.ssnagin.collectionmanager.gui.commands.GUICommand;
import com.ssnagin.collectionmanager.gui.commands.commands.*;
import com.ssnagin.collectionmanager.gui.controllers.GUIController;
import com.ssnagin.collectionmanager.gui.nodes.logger.GUITextLogger;
import com.ssnagin.collectionmanager.gui.nodes.loginbar.LoginBar;
import com.ssnagin.collectionmanager.gui.nodes.table.main.GUITableMain;
import com.ssnagin.collectionmanager.locales.LangLocalesAdapter;
import com.ssnagin.collectionmanager.user.objects.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.Locale;

public class MainGUIController extends GUIController {

    private CommandManager globalCommandManager;

    // TABLE COLUMNS

    @FXML
    protected void initialize() {

        if (isInitialized) return;
        isInitialized = true;

        initEventListeners();

        globalCommandManager = CommandManager.getInstance();

        initGUICommands();


//        animationArea.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            windowManager.get("animation").show();
//            ((GUICommandShow) localCommandManager.get("gui_show")).executeCommand(guiTableMain);
//        });

        // В самом конце -- бросим GUI_CONTENT_LOADED

        eventManager.publish(EventType.GUI_CONTENT_LOADED.toString(), null);
    }

    private void initGUICommands() {
//        localCommandManager.register(new GUICommandHelp("gui_help", localCommandManager));
//        localCommandManager.register(new GUICommandHistory("gui_history", localCommandManager));
//
//        localCommandManager.register(new GUICommandAuth("gui_auth", networking, windowManager));
//        localCommandManager.register(new GUICommandLogout("gui_logout", networking));
//
//        localCommandManager.register(new GUICommandShow("gui_show", networking));
//
//        localCommandManager.register(new GUICommandAdd("gui_add", networking, windowManager));
//        localCommandManager.register(new GUICommandRemoveById("gui_remove", networking, leftTextArea));
//        localCommandManager.register(new GUICommandRandom("gui_random", networking, leftTextArea));
//
//        localCommandManager.register(new GUICommandCountMembersById("gui_count_members_by_id", networking, leftTextArea));
//        localCommandManager.register(new GUICommandClear("gui_clear", networking, leftTextArea));
//
//        localCommandManager.register(new GUICommandExecuteScript("gui_execute_script", (UserCommand) globalCommandManager.get("execute_script")));
    }


    @Override
    protected void initEventListeners() {
//        eventManager.subscribe(EventType.USER_LOGGED_IN.toString(),
//                this::handleUserLoggedIn);
//
//        eventManager.subscribe(EventType.USER_LOGGED_OUT.toString(),
//                this::handleUserLoggedOut);
//
//        eventManager.subscribe(EventType.COLLECTION_DATA_CHANGED.toString(),
//                this::handleTableContentRefresh);
//
//        // EXECUTE_SCRIPT ended
//
//        eventManager.subscribe(EventType.SCRIPTS_HAVE_BEEN_EXECUTED.toString(),
//                this::handleScriptsHaveBeenExecuted);
//
//        // GUI CONTENT LOADED
//
//        eventManager.subscribe(EventType.GUI_CONTENT_LOADED.toString(),
//                this::handleGUIContentLoaded);
//
//        textLogger = new GUITextLogger(eventManager, leftTextArea);
    }
}

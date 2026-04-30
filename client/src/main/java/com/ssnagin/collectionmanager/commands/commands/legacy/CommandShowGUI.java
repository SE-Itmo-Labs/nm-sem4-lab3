/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ssnagin.collectionmanager.commands.commands.legacy;

import com.ssnagin.collectionmanager.applicationstatus.ApplicationStatus;
import com.ssnagin.collectionmanager.commands.UserCommand;
import com.ssnagin.collectionmanager.console.ClientConsole;
import com.ssnagin.collectionmanager.gui.window.Window;
import com.ssnagin.collectionmanager.gui.window.WindowManager;
import com.ssnagin.collectionmanager.inputparser.ParsedString;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Throws when other commands does not exist. The only one unregistered command!
 *
 * @author developer
 */
public class CommandShowGUI extends UserCommand {

    private WindowManager windowManager;

    private String USAGE = "gui <argument>\n\nshow  | shows graphics for CollectionManager\nhide  | hides it";

    public CommandShowGUI(String name, String description, WindowManager windowManager) {
        super(name, description);

        this.windowManager = windowManager;
    }

    @Override
    public ApplicationStatus executeCommand(ParsedString parsedString) {

        ApplicationStatus applicationStatus = super.executeCommand(parsedString);
        if (applicationStatus != ApplicationStatus.RUNNING) return applicationStatus;

        try {
            String action = parsedString.getArguments().get(0);

            Window mainWindow = this.windowManager.get("main");
            if (mainWindow == null) throw new NoSuchElementException("GUI Window have not been initiated | Internal error");

            if (action.toLowerCase().equals("hide")) {

                for (Window window : windowManager.getAll()) window.hide();

                return applicationStatus;
            }

            mainWindow.show();
            return applicationStatus;

        } catch (NoSuchElementException e) {
            ClientConsole.error(e);
            return ApplicationStatus.RUNNING;
        } catch (IndexOutOfBoundsException e) {
            return showUsage(parsedString);
        } catch (Exception e) {
            ClientConsole.error("UNCHECKED ERROR: " + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            return applicationStatus;
        }

//        try {
//            if (parsedString.getArguments().get(0).equalsIgnoreCase("show")) {
//
//                if (clientGUI.isGUIRunning()) {
//                    clientGUI.showGUI();
//                    return ApplicationStatus.RUNNING;
//                }
//
//                clientGUI.launchGUI();
//                return ApplicationStatus.RUNNING;
//            }
//
//            if (parsedString.getArguments().get(0).equalsIgnoreCase("hide")) {
//                clientGUI.hideGUI();
//                return ApplicationStatus.RUNNING;
//            }
//        } catch (IndexOutOfBoundsException e) {return showUsage(parsedString);}
//        catch (Exception e) {
//            ClientConsole.error(e.getMessage());
//            return ApplicationStatus.RUNNING;
//        }
    }

    @Override
    public ApplicationStatus showUsage(ParsedString parsedString) {
        ClientConsole.println(USAGE);
        return ApplicationStatus.RUNNING;
    }
}
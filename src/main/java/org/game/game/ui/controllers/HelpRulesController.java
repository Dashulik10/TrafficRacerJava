package org.game.game.ui.controllers;

import org.game.game.GameApplication;

public class HelpRulesController {
    public void handleBackToMenu() {
        GameApplication.setRoot("/fxml/MenuView.fxml");
    }
}
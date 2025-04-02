package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.game.game.GameApplication;

public class MenuController {
    @FXML
    private Button startGameButton;

    @FXML
    private void startGame() {
        GameApplication.showGameWindow();
    }

    @FXML
    private void handleStartGame() {
        GameApplication.setRoot("/fxml/ConfigView.fxml");
    }

    @FXML
    private void handleSettings() {
        GameApplication.setRoot("/fxml/ConfigView.fxml");
    }

    @FXML
    private void handleShowRecords() {
        GameApplication.setRoot("/fxml/TableView.fxml");
    }

    @FXML
    private void handleShowHelp() {
        GameApplication.setRoot("/fxml/RulesView.fxml");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
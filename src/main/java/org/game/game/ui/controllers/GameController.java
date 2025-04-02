package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.game.game.ui.scenes.GameWindow;

public class GameController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                stage.setTitle("Traffic Racer Game");

                GameWindow gameWindow = new GameWindow();
                Scene gameScene = gameWindow.createGameScene();
                stage.setScene(gameScene);
            }
        });
    }
}
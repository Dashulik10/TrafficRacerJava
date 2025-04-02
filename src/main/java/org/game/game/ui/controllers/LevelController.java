package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.game.game.GameApplication;
import org.game.game.core.engine.GameStartConfig;
import org.game.game.core.engine.DifficultyMode;

public class LevelController {

    @FXML
    private RadioButton noTrafficRadio;

    @FXML
    private RadioButton withTrafficRadio;

    @FXML
    private RadioButton timeChallengeRadio;

    @FXML
    private Label selectedCarLabel;

    @FXML
    private Label selectedColorLabel;

    private ToggleGroup difficultyToggleGroup;

    private String selectedCar = "Легковая";
    private String selectedColor = "Синий";

    @FXML
    public void initialize() {
        difficultyToggleGroup = new ToggleGroup();

        noTrafficRadio.setToggleGroup(difficultyToggleGroup);
        noTrafficRadio.setUserData("NO_TRAFFIC");

        withTrafficRadio.setToggleGroup(difficultyToggleGroup);
        withTrafficRadio.setUserData("WITH_TRAFFIC");

        timeChallengeRadio.setToggleGroup(difficultyToggleGroup);
        timeChallengeRadio.setUserData("TIME_CHALLENGE");

        noTrafficRadio.setSelected(true);
    }

    @FXML
    private void handleAccept() {
        String selectedDifficulty = difficultyToggleGroup.getSelectedToggle().getUserData().toString();
        DifficultyMode mode = DifficultyMode.valueOf(selectedDifficulty);
        GameStartConfig.setSelectedDifficulty(mode);
        GameStartConfig.setSelectedCar(selectedCar);
        GameStartConfig.setCarColor(selectedColor);

        GameApplication.setRoot("/fxml/GameView.fxml");
    }

    @FXML
    private void handleCancel() {
        GameApplication.setRoot("/fxml/MenuView.fxml");
    }

    @FXML
    private void handleSelectCar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectCarView.fxml"));
            Parent root = loader.load();

            SelectCarController selectCarController = loader.getController();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Выбор машины");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            String car = selectCarController.getSelectedCar();
            if (car != null) {
                selectedCar = car;
                selectedCarLabel.setText(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSelectColor() {
        if (!selectedCar.equals("Легковая")) {
            new Alert(Alert.AlertType.WARNING, "Цвет можно настроить только для легковой машины.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectColorView.fxml"));
            Parent root = loader.load();

            SelectColorController selectColorController = loader.getController();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Выбор цвета");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            String color = selectColorController.getSelectedColor();
            if (color != null) {
                selectedColor = color;
                selectedColorLabel.setText(color); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
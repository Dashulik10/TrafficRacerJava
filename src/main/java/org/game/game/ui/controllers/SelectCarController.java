package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SelectCarController {

    @FXML
    private ImageView carOne;

    @FXML
    private ImageView carTwo;

    @FXML
    private ImageView carThree;

    @FXML
    private ImageView carFour;

    @FXML
    private Button cancelButton;

    private String selectedCar = null;

    public String getSelectedCar() {
        return selectedCar;
    }

    @FXML
    public void initialize() {
        carOne.setImage(new Image(getClass().getResource("/images/blue.png").toExternalForm()));
        carTwo.setImage(new Image(getClass().getResource("/images/police.png").toExternalForm()));
        carThree.setImage(new Image(getClass().getResource("/images/truck.png").toExternalForm()));
        carFour.setImage(new Image(getClass().getResource("/images/911.png").toExternalForm()));

        carOne.setOnMouseClicked(event -> handleCarSelection("Легковая"));
        carTwo.setOnMouseClicked(event -> handleCarSelection("Полицейская"));
        carThree.setOnMouseClicked(event -> handleCarSelection("Грузовик"));
        carFour.setOnMouseClicked(event -> handleCarSelection("Экстренная"));
    }

    private void handleCarSelection(String carName) {
        selectedCar = carName;
        Stage stage = (Stage) carOne.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
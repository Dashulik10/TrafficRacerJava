package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SelectColorController {

    @FXML
    private ImageView colorOne;

    @FXML
    private ImageView colorTwo;

    @FXML
    private ImageView colorThree;

    @FXML
    private ImageView colorFour;

    @FXML
    private ImageView colorFive;

    @FXML
    private Button cancelButton;

    private String selectedColor = null;

    public String getSelectedColor() {
        return selectedColor;
    }

    @FXML
    public void initialize() {
        colorOne.setImage(new Image(getClass().getResource("/images/blue.png").toExternalForm()));   // Синий
        colorTwo.setImage(new Image(getClass().getResource("/images/green.png").toExternalForm()));  // Зеленый
        colorThree.setImage(new Image(getClass().getResource("/images/yellow.png").toExternalForm())); // Желтый
        colorFour.setImage(new Image(getClass().getResource("/images/pink.png").toExternalForm()));  // Розовый
        colorFive.setImage(new Image(getClass().getResource("/images/orange.png").toExternalForm())); // Оранжевый

        colorOne.setOnMouseClicked(event -> handleColorSelection("Синий"));
        colorTwo.setOnMouseClicked(event -> handleColorSelection("Зеленый"));
        colorThree.setOnMouseClicked(event -> handleColorSelection("Желтый"));
        colorFour.setOnMouseClicked(event -> handleColorSelection("Розовый"));
        colorFive.setOnMouseClicked(event -> handleColorSelection("Оранжевый"));
    }

    private void handleColorSelection(String color) {
        selectedColor = color;
        Stage stage = (Stage) colorOne.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
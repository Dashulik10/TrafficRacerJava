package org.game.game.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.game.game.core.engine.DifficultyMode;
import org.game.game.core.models.Player;
import org.game.game.data.GameRecordDAO;

import java.io.IOException;

public class NewRecordController {

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    private int score;
    private DifficultyMode mode;

    public static void showNewRecordWindow(int score, DifficultyMode mode) {

        System.out.println("Открывается окно нового рекорда.");
        try {
            FXMLLoader loader = new FXMLLoader(NewRecordController.class.getResource("/fxml/NewRecordeWindow.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            NewRecordController controller = loader.getController();
            controller.initData(score, mode);

            stage.setTitle("Поздравляем с рекордом!");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(int score, DifficultyMode mode) {
        this.score = score;
        this.mode = mode;
    }

    @FXML
    private void onSave() {
        String playerName = nameField.getText().trim();
        if (!playerName.isEmpty()) {
            Player player = new Player(playerName, score);
            GameRecordDAO dao = new GameRecordDAO();
            dao.checkAndSaveRecord(player);

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }
}
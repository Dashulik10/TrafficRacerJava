package org.game.game.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.game.game.data.DataBaseConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TableController {

    @FXML
    private TableView<Record> recordsTable;

    @FXML
    private TableColumn<Record, String> playerColumn;

    @FXML
    private TableColumn<Record, Integer> scoreColumn;

    @FXML
    private TableColumn<Record, String> gameModeColumn;

    private final ObservableList<Record> recordList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        gameModeColumn.setCellValueFactory(new PropertyValueFactory<>("gameMode"));

        loadRecords();

        recordsTable.setItems(recordList);
    }

    @FXML
    private void handleBack() {
        org.game.game.GameApplication.setRoot("/fxml/MenuView.fxml");
    }

    private void loadRecords() {
        String query = "SELECT player_name, score, game_mode FROM game_records ORDER BY score DESC";

        try (Connection connection = DataBaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String playerName = resultSet.getString("player_name");
                int score = resultSet.getInt("score");
                String gameMode = resultSet.getString("game_mode");

                recordList.add(new Record(playerName, score, gameMode));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Record {
        private final String playerName;
        private final int score;
        private final String gameMode;

        public Record(String playerName, int score, String gameMode) {
            this.playerName = playerName;
            this.score = score;
            this.gameMode = gameMode;
        }

        public String getPlayerName() {
            return playerName;
        }
        public int getScore() {
            return score;
        }

        public String getGameMode() {
            return gameMode;
        }
    }
}
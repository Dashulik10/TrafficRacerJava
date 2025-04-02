package org.game.game.data;

import org.game.game.core.engine.DifficultyMode;
import org.game.game.core.engine.GameStartConfig;
import org.game.game.core.models.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameRecordDAO {
    public void checkAndSaveRecord(Player player) {
        DifficultyMode currentMode = GameStartConfig.getSelectedDifficulty();

        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(
                     "SELECT MAX(score) FROM game_records WHERE game_mode = ?");
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO game_records (player_name, score, game_mode) VALUES (?, ?, ?)")) {

            selectStatement.setString(1, currentMode.name());
            ResultSet resultSet = selectStatement.executeQuery();

            boolean shouldInsert = true;
            if (resultSet.next() && resultSet.getInt(1) >= player.getScore()) {
                shouldInsert = false;
            }

            if (shouldInsert) {
                insertStatement.setString(1, player.getName());
                insertStatement.setInt(2, player.getScore());
                insertStatement.setString(3, currentMode.name());
                insertStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isNewRecord(int score, DifficultyMode mode) {
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(
                     "SELECT MAX(score) FROM game_records WHERE game_mode = ?")) {

            selectStatement.setString(1, mode.name());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int maxScore = resultSet.getInt(1);
                return score > maxScore;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }
}
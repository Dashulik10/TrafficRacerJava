package org.game.game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.game.game.core.engine.SoundManager;
import org.game.game.ui.scenes.GameWindow;

import java.io.IOException;

public class GameApplication extends Application {

    private static Stage stage;
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        showMenu();

        primaryStage.setTitle("Traffic Racer Game");
        primaryStage.setWidth(906);
        primaryStage.setHeight(828);
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void showMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(GameApplication.class.getResource("/fxml/MenuView.fxml"));
        Parent root = loader.load();

        if (scene == null) {
            scene = new Scene(root);
        } else {
            scene.setRoot(root);
        }

        resetStyles();

        stage.setScene(scene);
        stage.setWidth(906);
        stage.setHeight(828);
        stage.setResizable(false);

        root.requestFocus();
        Platform.runLater(() -> stage.sizeToScene());

    }

    public static void resetStyles() {
        if (scene != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(GameApplication.class.getResource("/css/menu.css").toExternalForm());

            scene.setFill(null);
        }
    }

    public static void showGameWindow() {
        GameWindow gameWindow = new GameWindow();
        Scene gameScene = gameWindow.createGameScene();
        stage.setScene(gameScene);

        stage.setWidth(906);
        stage.setHeight(828);
    }


    public static void setRoot(String fxmlFilePath) {
        try {
            if (fxmlFilePath == null || fxmlFilePath.isEmpty()) {
                throw new IllegalArgumentException("FXML file path cannot be null or empty");
            }

            Parent root = FXMLLoader.load(GameApplication.class.getResource(fxmlFilePath));

            if (scene == null) {
                throw new IllegalStateException("Scene is not initialized. Call showMenu() first.");
            }

            scene.setRoot(root);
            scene.getStylesheets().clear();

            if (fxmlFilePath.contains("/fxml/RulesView.fxml")) {
                scene.getStylesheets().add(GameApplication.class.getResource("/css/rules.css").toExternalForm());
            }
            else if (fxmlFilePath.contains("/fxml/ConfigView.fxml")) {
                scene.getStylesheets().add(GameApplication.class.getResource("/css/config.css").toExternalForm());
            }
            else if (fxmlFilePath.contains("/fxml/TableView.fxml")) {
                scene.getStylesheets().add(GameApplication.class.getResource("/css/table.css").toExternalForm());
            }
            else if (fxmlFilePath.contains("/fxml/NewRecordeWindow.fxml")) {
                scene.getStylesheets().add(GameApplication.class.getResource("/css/record.css").toExternalForm());
            }else {
                scene.getStylesheets().add(GameApplication.class.getResource("/css/menu.css").toExternalForm());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Не удалось загрузить ресурс");
            alert.setContentText("FXML файл не найден: " + fxmlFilePath);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Произошла непредвиденная ошибка");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        }
}

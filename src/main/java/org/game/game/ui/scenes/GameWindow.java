package org.game.game.ui.scenes;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.game.game.core.engine.SoundManager;
import org.game.game.GameApplication;
import org.game.game.core.engine.GameLogic;
import org.game.game.core.engine.GameStartConfig;
import org.game.game.core.models.Car;
import org.game.game.core.engine.DifficultyMode;
import org.game.game.core.models.CarModel;
import org.game.game.data.GameRecordDAO;
import org.game.game.services.CarModelService;
import org.game.game.ui.controllers.NewRecordController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameWindow {
    private GameLogic gameLogic;
    private Label scoreText, timeText, speedText;
    private Car playerCar;
    private List<Car> obstacles = new ArrayList<>();
    private Random random = new Random();
    private AnimationTimer timer;
    private Pane root;
    private ImageView road1;
    private ImageView road2;
    private final double ROAD_HEIGHT = 800;
    private double roadSpeed = 0;
    private SoundManager soundManager;

    public GameWindow() {
        this.soundManager = SoundManager.getInstance();
    }

    private boolean isGameOver = false;

    public Scene createGameScene() {
        root = new Pane();

        setupBackground(root);

        DifficultyMode mode = GameStartConfig.getSelectedDifficulty();
        gameLogic = new GameLogic(mode);

        setupTextLabels();

        setupPlayerCar();

        Scene scene = new Scene(root, 906, 800);
        setupControls(scene);

        setupGameAnimation(scene);
        scene.setOnMouseClicked(event -> root.requestFocus());

        return scene;
    }

    private void setupTextLabels() {
        scoreText = new Label("Очки: 0");
        scoreText.setLayoutX(10);
        scoreText.setLayoutY(10);
        scoreText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FF4500;");

        timeText = new Label("Время: 0");
        timeText.setLayoutX(10);
        timeText.setLayoutY(50);
        timeText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00BFFF;");

        speedText = new Label("Скорость: 0");
        speedText.setLayoutX(10);
        speedText.setLayoutY(90);
        speedText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #7CFC00;");

        root.getChildren().addAll(scoreText, timeText, speedText);
    }

    private void setupPlayerCar() {
        CarModelService carModelService = new CarModelService();

        String selectedCar = GameStartConfig.getSelectedCar();
        String selectedColor = GameStartConfig.getCarColor();

        CarModel carModel = carModelService.getCarModelByName(selectedCar);

        String imagePath;
        if ("Легковая".equals(selectedCar)) {
            imagePath = carModelService.getImagePathByColor(selectedColor);
        } else {
            imagePath = carModel.getImagePath();
        }

        double visibleHeight = 150;
        double startingY = 800 - visibleHeight - 20;

        playerCar = new Car(450, startingY, carModel.getWidth(), carModel.getHeight(), imagePath);
        playerCar.setSpeed(50);
        root.getChildren().add(playerCar.getShape());
    }

    private void setupControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            double currentX = playerCar.getShape().getLayoutX();

            if (code == KeyCode.UP) {
                playerCar.setSpeed(playerCar.getSpeed() + 10);
            } else if (code == KeyCode.DOWN) {
                double newSpeed = Math.max(20, playerCar.getSpeed() - 10);
                playerCar.setSpeed(newSpeed);
                System.out.println("Speed decreased: " + newSpeed);
            }

            if (code == KeyCode.LEFT && currentX > 152) {
                movePlayerHorizontally(currentX - 100);
            } else if (code == KeyCode.RIGHT && currentX < 720) {
                movePlayerHorizontally(currentX + 100);
            }
        });
    }

    private void movePlayerHorizontally(double targetX) {
        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentX = playerCar.getShape().getLayoutX();
                double step = 5;

                if (currentX < targetX) {
                    playerCar.getShape().setLayoutX(Math.min(targetX, currentX + step));
                } else if (currentX > targetX) {
                    playerCar.getShape().setLayoutX(Math.max(targetX, currentX - step));
                }

                if (Math.abs(currentX - targetX) < step) {
                    stop();
                }
            }
        };
        moveTimer.start();
    }

    private void updateObstacles() {
        Iterator<Car> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Car obstacle = iterator.next();

            obstacle.moveDown(obstacle.getSpeed() / 60.0 + roadSpeed / 2.0);

            if (obstacle.getShape().getLayoutY() >= root.getHeight() + obstacle.getShape().getFitHeight()) {
                root.getChildren().remove(obstacle.getShape());
                iterator.remove();
            }
        }

        double scaledSpawnRate = gameLogic.getSpawnRate() * (playerCar.getSpeed() / 100.0);
        if (random.nextDouble() < Math.min(1, scaledSpawnRate)) {
            addCounterFlowObstacle();
        }
    }

    private void addCounterFlowObstacle() {
        int lane = random.nextInt(4);

        double posX = switch (lane) {
            case 0 -> 120;
            case 1 -> 310;
            case 2 -> 529;
            case 3 -> 720;
            default -> 152;
        };

        CarModelService carModelService = new CarModelService();
        CarModel carModel = carModelService.getAllCarModels()
                .get(random.nextInt(carModelService.getAllCarModels().size()));

        double posY = -120;

        Car obstacle = new Car(posX, posY, carModel.getWidth(), carModel.getHeight(), carModel.getImagePath());

        double randomSpeedMultiplier = 1 + random.nextDouble();
        obstacle.setSpeed((100 + random.nextDouble() * 50) * randomSpeedMultiplier);

        if ((lane == 0 || lane == 1) && gameLogic.getMode() == DifficultyMode.WITH_TRAFFIC) {
            obstacle.getShape().setRotate(180);
        }

        obstacles.add(obstacle);
        root.getChildren().add(obstacle.getShape());
    }

    private void setupGameAnimation(Scene scene) {
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000 / 60) {
                    updateGame();
                    gameLogic.update(now);

                    scoreText.setText("Очки: " + gameLogic.getScore());
                    if (gameLogic.getMode() == DifficultyMode.TIME_CHALLENGE) {
                        timeText.setText("Время: " + String.format("%.2f", Math.max(0, gameLogic.getTimeLeft())) + " сек.");
                    } else {
                        timeText.setText("Время: " + String.format("%.2f", gameLogic.getElapsedTime() / 1000.0) + " сек.");
                    }
                    speedText.setText("Скорость: " + playerCar.getSpeed() + " км/ч");

                    updateObstacles();
                    checkCollisions();

                    if (gameLogic.getMode() == DifficultyMode.TIME_CHALLENGE && gameLogic.getTimeLeft() <= 0) {
                        try {
                            endGameOnTime();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void endGameOnTime() throws IOException {

        if (isGameOver) return;
        isGameOver = true;

        if (timer != null) {
            timer.stop();
        }

        Platform.runLater(() -> {
            new Alert(Alert.AlertType.INFORMATION, "Время истекло! Ваш счёт: " + gameLogic.getScore()).showAndWait();
            try {
                GameApplication.showMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleCollision(Car obstacle) {
        playerCar.setSpeed(0);
        obstacle.setSpeed(0);

        playCollisionEffect(obstacle);


        soundManager.playSoundEffect("collision", "/sounds/stolknovenie.mp3");

    }

    private void playCollisionEffect(Car obstacle) {
        obstacle.getShape().setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.7, 0, 0);");
        playerCar.getShape().setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.7, 0, 0);");

        new Thread(() -> {
            Platform.runLater(() -> {
                obstacle.getShape().setStyle("");
                playerCar.getShape().setStyle("");
            });
        }).start();
    }

    private void checkCollisions() {
        for (Car obstacle : obstacles) {
            if (playerCar.getShape().getBoundsInParent().intersects(obstacle.getShape().getBoundsInParent())) {
                if (gameLogic.getMode() == DifficultyMode.TIME_CHALLENGE) {
                    handleCollision(obstacle);
                } else {
                    try {
                        endGame(obstacle);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    private void endGame(Car collidedCar) throws IOException {

        if (isGameOver) return;
        System.out.println("Игра завершена. Проверяем новый рекорд...");
        isGameOver = true;

        timer.stop();

        soundManager.playSoundEffect("gameOver", "/sounds/avaria.mp3");

        double x = collidedCar.getShape().getLayoutX();
        double y = collidedCar.getShape().getLayoutY();
        double width = collidedCar.getShape().getFitWidth();
        double height = collidedCar.getShape().getFitHeight();

        showExplosion(x, y, width, height);

        GameRecordDAO dao = new GameRecordDAO();
        boolean isNewRecord = dao.isNewRecord(gameLogic.getScore(), gameLogic.getMode());

        if (isNewRecord) {
            Platform.runLater(() -> {
                NewRecordController.showNewRecordWindow(gameLogic.getScore(), gameLogic.getMode());
            });
        }

        Platform.runLater(() -> {
            new Alert(Alert.AlertType.INFORMATION, "Игра окончена! Ваш счёт: " + gameLogic.getScore()).showAndWait();
            try {
                GameApplication.showMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showExplosion(double x, double y, double width, double height) {
        ImageView explosionImage = new ImageView(new Image("D:\\university\\pioivis\\sem_4\\PPOIS\\Game\\src\\main\\resources\\images\\boom.gif"));
        explosionImage.setLayoutX(x - width / 2);
        explosionImage.setLayoutY(y - height / 2);
        explosionImage.setFitWidth(width * 3);
        explosionImage.setFitHeight(height * 3);

        root.getChildren().add(explosionImage);

        ScaleTransition scaleTransition = new ScaleTransition(javafx.util.Duration.seconds(1), explosionImage);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(3);
        scaleTransition.setToY(3);

        FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.seconds(1.2), explosionImage);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        javafx.animation.ParallelTransition explosionAnimation = new javafx.animation.ParallelTransition(scaleTransition, fadeTransition);

        explosionAnimation.setOnFinished(event -> root.getChildren().remove(explosionImage));
        explosionAnimation.play();
    }

    private void updateGame() {
        roadSpeed = playerCar.getSpeed() / 10;
        moveBackground();
        updateObstacles();
        checkCollisions();
        speedText.setText("Скорость: " + playerCar.getSpeed() + " км/ч");
    }


    private void setupBackground(Pane root) {
        Image roadImage = new Image("D:\\university\\pioivis\\sem_4\\PPOIS\\Game\\src\\main\\resources\\images\\road.png");

        road1 = new ImageView(roadImage);
        road2 = new ImageView(roadImage);

        road1.setFitWidth(906);
        road1.setFitHeight(800);

        road2.setFitWidth(906);
        road2.setFitHeight(800);
        road2.setLayoutY(-800);

        root.getChildren().addAll(road1, road2);
    }

    private void moveBackground() {
        road1.setLayoutY(road1.getLayoutY() + roadSpeed);
        road2.setLayoutY(road2.getLayoutY() + roadSpeed);

        if (road1.getLayoutY() >= 800) {
            road1.setLayoutY(road2.getLayoutY() - 800);
        }

        if (road2.getLayoutY() >= 800) {
            road2.setLayoutY(road1.getLayoutY() - 800);
        }
    }
}
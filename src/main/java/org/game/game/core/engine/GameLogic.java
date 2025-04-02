package org.game.game.core.engine;

import org.game.game.data.GameRecordDAO;
import org.game.game.ui.controllers.NewRecordController;

public class GameLogic {

    private int score = 0;
    private double speed = 50;
    private double distance = 0;
    private long startTime;
    private long lastTime = 0; // Предыдущее время, когда производилось обновление
    private DifficultyMode mode;

    private double timeLimit = 60;

    public GameLogic(DifficultyMode mode) {
        this.startTime = System.currentTimeMillis(); //Старт времени
        this.mode = mode;
    }

    public void update(long now) {
        if (lastTime > 0) {
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // время с прошлого обновления

            distance += speed * deltaTime / 3.6;

            // Установка опред подсчёта очков для каждого режима
            switch (mode) {
                case NO_TRAFFIC -> calculateNoTrafficScore(deltaTime);
                case WITH_TRAFFIC -> calculateWithTrafficScore(deltaTime);
                case TIME_CHALLENGE -> {
                    calculateNoTrafficScore(deltaTime);
                    timeLimit -= deltaTime;
                    if (timeLimit <= 0) {
                        endGameDueToTime();
                    }
                }
            }
        }
        lastTime = now;
    }

    private void endGameDueToTime() {
        speed = 0;
        System.out.println("Игра окончена: время истекло!");
    }

    private void calculateNoTrafficScore(double deltaTime) {
        if (speed > 100) {
            score +=  distance + deltaTime + 1000;
        } else {
            score += distance + deltaTime;
        }
    }

    private void calculateWithTrafficScore(double deltaTime) {
        if (speed > 100) {
            score +=  (2 * (distance + deltaTime)) + 1000;
        } else {
            score += 2 * (distance + deltaTime);
        }
    }

    // Время с момента старта
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public int getScore() {
        return score;
    }

    public DifficultyMode getMode() {
        return mode;
    }

    public double getTimeLeft() {
        return timeLimit;
    }

    public double getSpawnRate() {
        return switch (mode) {
            case NO_TRAFFIC -> 0.002;
            case WITH_TRAFFIC -> 0.004;
            case TIME_CHALLENGE -> 0.003;
        };
    }
}
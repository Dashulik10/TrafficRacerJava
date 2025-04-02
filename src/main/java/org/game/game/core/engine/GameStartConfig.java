package org.game.game.core.engine;

public class GameStartConfig {
    private static DifficultyMode selectedDifficulty = DifficultyMode.NO_TRAFFIC;
    private static String selectedCar = "Легковая";
    private static String carColor = "Синий";

    public static void setSelectedDifficulty(DifficultyMode difficulty) {
        selectedDifficulty = difficulty;
    }

    public static DifficultyMode getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public static void setSelectedCar(String car) {
        selectedCar = car;
    }

    public static String getSelectedCar() {
        return selectedCar;
    }

    public static void setCarColor(String color) {
        carColor = color;
    }

    public static String getCarColor() {
        return carColor;
    }
}
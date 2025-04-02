package org.game.game.core.models;

public class CarModel {
    private final String name;
    private final int width;
    private final int height;
    private final String imagePath;

    public CarModel(String name, int width, int height, String imagePath) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getImagePath() {
        return imagePath;
    }
}
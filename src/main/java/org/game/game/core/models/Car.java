package org.game.game.core.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car {
    private ImageView shape;
    private double speed;

    public Car(double posX, double posY, double width, double height, String imagePath) {
        this.shape = new ImageView(new Image(imagePath));
        this.shape.setFitWidth(width);
        this.shape.setFitHeight(height);
        this.shape.setLayoutX(posX);
        this.shape.setLayoutY(posY);
        this.speed = 0;
    }

    public ImageView getShape() {
        return shape;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(0, Math.min(300, speed));
        System.out.println("Current speed: " + this.speed);
    }

    public double getSpeed() {
        return speed;
    }

    // Перемещение машины вниз
    public void moveDown(double distance) {
        shape.setLayoutY(shape.getLayoutY() + distance);
    }
}
package org.game.game.services;

import org.game.game.core.models.CarModel;

import java.util.List;
import java.util.Map;

public class CarModelService {

    private static final String IMAGE_BASE_PATH = "D:/university/pioivis/sem_4/PPOIS/Game/src/main/resources/images/";

    private final List<CarModel> carModels = List.of(
            new CarModel("Легковая", 94, 188, IMAGE_BASE_PATH + "blue.png"),
            new CarModel("Синяя", 94, 188, IMAGE_BASE_PATH + "blue.png"),
            new CarModel("Экстренная", 99, 198, IMAGE_BASE_PATH + "911.png"),
            new CarModel("Зеленая", 89, 180, IMAGE_BASE_PATH + "green.png"),
            new CarModel("Оранжевая", 88, 174, IMAGE_BASE_PATH + "orange.png"),
            new CarModel("Розовая", 93, 189, IMAGE_BASE_PATH + "pink.png"),
            new CarModel("Полицейская", 90, 178, IMAGE_BASE_PATH + "police.png"),
            new CarModel("Грузовик", 107, 293, IMAGE_BASE_PATH + "truck.png"),
            new CarModel("Желтая", 86, 172, IMAGE_BASE_PATH + "yellow.png")
    );

    private final Map<String, String> colorToImageMap = Map.of(
            "Синий", IMAGE_BASE_PATH + "blue.png",
            "Зеленый", IMAGE_BASE_PATH + "green.png",
            "Желтый", IMAGE_BASE_PATH + "yellow.png",
            "Розовый", IMAGE_BASE_PATH + "pink.png",
            "Оранжевый", IMAGE_BASE_PATH + "orange.png"
    );

    public List<CarModel> getAllCarModels() {
        return carModels;
    }

    public CarModel getCarModelByName(String name) {
        return carModels.stream()
                .filter(model -> model.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Модель машины не найдена: " + name));
    }

    public String getImagePathByColor(String color) {
        return colorToImageMap.getOrDefault(color, "D:\\university\\pioivis\\sem_4\\PPOIS\\Game\\src\\main\\resources\\images\\blue.png"); // Устанавливаем синий по умолчанию
    }
}

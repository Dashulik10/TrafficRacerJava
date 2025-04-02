package org.game.game.core.engine;

import javafx.scene.media.Media; // для загрузки
import javafx.scene.media.MediaPlayer; // для воспроизведения


public class SoundManager {
    private static SoundManager instance;

    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playSoundEffect(String effectName, String path) {
        MediaPlayer player = new MediaPlayer(new Media(getClass().getResource(path).toExternalForm()));
        player.play();
    }
}
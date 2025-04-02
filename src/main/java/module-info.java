module org.game.game {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires javafx.media;

    exports org.game.game;
    exports org.game.game.ui.controllers to javafx.fxml;

    opens org.game.game to javafx.fxml;
    opens org.game.game.ui.controllers to javafx.fxml, javafx.base;
    exports org.game.game.ui.scenes;
    opens org.game.game.ui.scenes to javafx.fxml;
}
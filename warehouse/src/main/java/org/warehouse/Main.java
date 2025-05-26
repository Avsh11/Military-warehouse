package org.warehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/LoginPanel.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Login Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

// Używane biblioteki, JDK, pluginy, zależności:
// - JDK SE Development Kit 24.0.1 (64 Bit)
// - Reszta użytych zależności/bibliotek/modułów w pom.xml i module-info.
// - Większość brane z repo maven'a.

// UWAGI (dla mnie):
// Nie zapomnieć: Dodawanie onAction do przycisków, inputów w fmxl oraz podawania fx:id.

// Do zrobienia: funkcjonalność po stronie admina.

// Uporządkować ładnie zmienne w zależności do klasy - na potem najpierw funkcjonalność

// Na samym końcu css i jakkolwiek ładne gui
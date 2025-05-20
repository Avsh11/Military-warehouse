package org.warehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import static javafx.application.Application.launch;

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
    } // Koniec metody main.
} // Koniec klasy Main.

// Używane biblioteki, JDK, pluginy, zależności:
// - JDK SE Development Kit 24.0.1 (64 Bit)

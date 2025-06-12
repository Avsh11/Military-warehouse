package org.warehouse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/LoginPanel.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("C.A.S");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/logo.png"))));
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
// - UML Generator.

// UWAGI (dla mnie) - notatki:

// Wyformatować ładnie kod, spójność nazw fla zmiennych i metod ogarnąć, przejrzystość
// Dokończyć CSS:
// - tabelki w adminie + cały w userze żeby to jakoś wyglądało.
// Posprawdzać kod i potestować czy gdzieś formatki nie wywala cen, dat itd.
// rozbudować niektóre alerty
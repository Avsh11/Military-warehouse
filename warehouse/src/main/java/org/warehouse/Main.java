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

// Dla admina naprawić:
// Infinity money glitch
// Te same nazwy dla userów (malo czasu opcjonalnie juz)
// Formatowanie fundsów jest git
// Ewentualnie formatowanie daty
// Wszystko dać na Code > Reformat code żeby było wymaganie spełnione że kod w miarę ładny.
// Admin załatwiony + poprawki CSS ewentualne.

// Dla usera:
// Trzymać się konwencji nazywania metod i klas.
// Ogarnąć CSS spójny dla każdego panelu.
// Komentarze do niektórych metod.
// Dać reformat code żeby formatowanie kodu nie było paskudne.


// Funkcjonalności po obu stronach są teraz spójność nazewnictwa, komy, wcięcia to reformatka automatycznie i tyle.
// Wiadomo CSS ogarnąć na końcu.
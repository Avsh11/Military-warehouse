package org.warehouse.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.warehouse.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel {
    @FXML
    private TextField hashlogin;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    // Metoda do obsługi logowania - zarówno przycisk jak i obsługa loginu, hasła
    // Musimy sprawdzić czy poprawne są dane z pola hasła i loginu (dla stworzonych już użytkowników)
    // Walidacja danych będzie obejmować nowych użytkowników stworzonych przez admina na ten moment dla testów
    // jest tylko admin i jakiś zwykły user żeby pokazać różnicę między panelami głównymi.

    // Metoda pobiera dane z pól oraz pspradza czy pola nie są puste, łączy się z bazą
    // tworzy zapytanie sqlowe i na podstawie boola isAdmin otwiera okno admina albo usera.

    @FXML
    private void handleLogin() {
        String loginHash = hashlogin.getText();
        String PasswordField = password.getText();
        // Pierwsza walidacja - co, gdy pola są puste - wywali alert.
        if (loginHash.isEmpty() || PasswordField.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Login and password fields are empty . . . Please try again.");
            alert.showAndWait();
            return;
        }
        // Podłączenie do bazy.
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE loginHash = ? AND password = ?";
            // PreparedStatement dla bezpieczeństwa - SQL Injection, dobra praktyka zamiast wstawiać
            // login i hasło do zapytania.
            PreparedStatement loginst = conn.prepareStatement(sql);
            loginst.setString(1, loginHash);
            loginst.setString(2, PasswordField);
            ResultSet rs = loginst.executeQuery();
            // Obsługa boola isAdmin - typ użytkownika, który się loguje do aplikacji.
            if (rs.next()) {
                boolean isAdmin = rs.getBoolean("isAdmin");
                if (isAdmin) {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPanel.class.getResource("/gui/AdminPanel.fxml"));
                    Parent primaryFXML = fxmlLoader.load();
                    Scene scene = new Scene(primaryFXML);
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Admin panel opened."); // Wiadomość do debuggowania
                } else {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginPanel.class.getResource("/gui/UserPanel.fxml"));
                    Parent primaryFXML = fxmlLoader.load();
                    Scene scene = new Scene(primaryFXML);
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("User panel opened."); // Wiadomość do debuggowania
                }
                // Wyłapywanie błędów - obsługa wyjątków
            } else {
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Login failed");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

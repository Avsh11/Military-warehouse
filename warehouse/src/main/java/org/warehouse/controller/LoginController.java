package org.warehouse.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.warehouse.util.DBConnection;
import org.warehouse.util.UserInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    // ? ? ? ? ? ? ? ? ? ? ?

    @FXML
    private ImageView logoImage;

    private Image imageDefault;
    private Image imageMouse;

    @FXML
    public void initialize() {
        imageDefault = new Image(getClass().getResourceAsStream("/image/icons/logo.png"));
        imageMouse = new Image(getClass().getResourceAsStream("/image/icons/secret.png"));
        logoImage.setImage(imageDefault);
    }

    @FXML
    public void handleMouseEntered() {
        logoImage.setImage(imageMouse);
    }

    @FXML
    public void handleMouseExited() {
        logoImage.setImage(imageDefault);
    }

    // ? ? ? ? ? ? ? ? ? ? ?

    // Metoda do obsługi logowania - zarówno przycisk jak i obsługa loginu, hasła.
    // Musimy sprawdzić czy poprawne są dane z pola hasła i loginu (dla stworzonych już użytkowników).

    // Walidacja danych będzie obejmować nowych użytkowników stworzonych przez admina na ten moment dla testów
    // jest tylko admin i jakiś zwykły user żeby pokazać różnicę między panelami głównymi.

    // Metoda pobiera dane z pól oraz pspradza czy pola nie są puste, łączy się z bazą,
    // tworzy zapytanie sqlowe i na podstawie boola isAdmin otwiera okno admina albo usera.

    @FXML
    private void handleLogin() {
        String loginHash = loginField.getText();
        String PasswordField = passwordField.getText();

        // Pierwsza walidacja - co, gdy pola są puste - wywali alert.

        if (loginHash.isEmpty() || PasswordField.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Login or password field is empty. Please try again . . .");
            alert.showAndWait();
            return;
        }

        // Podłączenie do bazy.

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE loginHash = ? AND password = ?";

            // PreparedStatement dla bezpieczeństwa - SQL Injection, dobra praktyka zamiast wstawiać
            // login i hasło do zapytania.

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loginHash);
            stmt.setString(2, PasswordField);
            ResultSet result = stmt.executeQuery();

            // Obsługa boola isAdmin - typ użytkownika, który się loguje do aplikacji.

            if (result.next()) {

                // UserID - który user - o jakim ID ?

                UserInfo.userID = result.getInt("userID");
                boolean isAdmin = result.getBoolean("isAdmin");

                if (isAdmin) {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/gui/admin/AdminPanel.fxml"));
                    Parent primaryFXML = fxmlLoader.load();
                    Scene scene = new Scene(primaryFXML);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/gui/user/UserPanel.fxml"));
                    Parent primaryFXML = fxmlLoader.load();
                    Scene scene = new Scene(primaryFXML);
                    scene.getStylesheets().add(getClass().getResource("/css/user/UserPanel.css").toExternalForm());
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                }

                // Wyłapywanie błędów - obsługa wyjątków.

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect login or password. Please try again . . .");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Login failed. Please try again . . .");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } // Koniec metody.
} // Koniec klasy.

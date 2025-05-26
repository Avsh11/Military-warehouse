package org.warehouse.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    // Metody
    // Metoda do obsługi logowania - zarówno przycisk jak i obsługa loginu, hasła
    // Musimy sprawdzić czy poprawne są dane z pola hasła i loginu (dla stworzonych już użytkowników)
    // Walidacja danych będzie obejmować nowych użytkowników stworzonych przez admina na ten moment dla testów
    // jest tylko admin i jakiś zwykły user żeby pokazać różnicę między panelami głównymi.
    // Bez dodatkowych metod obsługi paneli. Zostaje jedna. Panel zakończony. Brakuje CSS i jest git.
    @FXML
    private void handleLogin() {
        String loginHash = hashlogin.getText();
        String PasswordField = password.getText();
        if (loginHash.isEmpty() || PasswordField.isEmpty()) {
            System.out.println("Login hash or password is empty. Try again.");
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE loginHash = ? AND password = ?";
            PreparedStatement loginst = conn.prepareStatement(sql);

            loginst.setString(1, loginHash);
            loginst.setString(2, PasswordField);
            ResultSet rs = loginst.executeQuery();

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
            } else {
            }
        } catch (SQLException e) {
            System.out.println("Error, login failed " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

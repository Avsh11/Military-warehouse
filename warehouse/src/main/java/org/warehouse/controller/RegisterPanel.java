package org.warehouse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.warehouse.model.Nationality;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RegisterPanel {
    @FXML
    private TextField registerLogin;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerConfirmPassword;
    @FXML
    private Button registerButton;
    @FXML
    private ComboBox<Nationality> registerNationality;
    @FXML
    private Button previousPanel;

    @FXML
    private void initialize() {
        List<Nationality> nationalities = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM nationality WHERE NOT nationalityID = 1";
            PreparedStatement loginst = conn.prepareStatement(sql);
            ResultSet rs = loginst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("nationalityID");
                String nationality = rs.getString("country");
                nationalities.add(new Nationality(id, nationality));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<Nationality> nationalityOptions = FXCollections.observableArrayList(nationalities);
        registerNationality.setItems(nationalityOptions);
        // Defaultowe zaznaczenie narodowości - uniknięcie błędu.
        registerNationality.getSelectionModel().select(0);
    }

    @FXML
    private void handleRegister() {
        String login = registerLogin.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        int nationalityID = registerNationality.getSelectionModel().getSelectedItem().getNationalityID();
        // Część walidacji - puste pola + wywołanie funkcji passwordValidation zawierającej walidacje.
        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wszystkie pola muszą być wypełnione.");
            alert.showAndWait();
        } else if (!handlePasswordValidation(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Hasło musi mieć minimum 8 znaków, zawierać przynajmniej jedną dużą literę, jedną cyfrę i jeden znak specjalny.");
            alert.showAndWait();
        } else {
            String sql = "INSERT INTO users (loginHash, password, creationDate, nationalityID, isAdmin) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, login);
                stmt.setString(2, password);
                stmt.setDate(3, Date.valueOf(LocalDate.now()));
                stmt.setInt(4, nationalityID);
                stmt.setBoolean(5, false);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New User");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome " + login);
                    alert.showAndWait();
                    System.out.println("Wstawiono nowy rekord!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // Metoda walidacji hasła
    private boolean handlePasswordValidation(String password) {
        if (password.length() < 8) return false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecialCharacter = true;
        }
        return hasUpper && hasDigit && hasSpecialCharacter;
    }

    @FXML
    private void handlePreviousPanel() throws IOException {
        Stage stage = (Stage) previousPanel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/AdminPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

}

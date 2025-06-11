package org.warehouse.controller.admin;

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
import java.util.ArrayList;
import java.util.List;

public class RegisterController {
    // final - zmienna nie będzie zmieniana będzie mieć jedną konkretną przypisaną wartość.
    public static final int MAX_BALANCE = 999999999;
    @FXML
    private TextField registerLogin;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerConfirmPassword;
    @FXML
    private Button registerButton;
    // Combobox
    @FXML
    private ComboBox<Nationality> registerNationality;
    @FXML
    private Button previousPageButton;

    // Metoda wykona się po wczytaniu paneul
    // Pobiera listę narodowości oprócz ID 1 - bo ta należy do admina i tylko admin ją ma.
    // Ustawiane w combo boxie.

    @FXML
    private void initialize() {
        List<Nationality> nationalities = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM nationality WHERE NOT nationalityID = 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("nationalityID");
                String nationality = result.getString("country");
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
    private void handleRegister() throws SQLException {
        String login = registerLogin.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        int nationalityID = registerNationality.getSelectionModel().getSelectedItem().getNationalityID();

        // Część walidacji - puste pola + wywołanie funkcji passwordValidation zawierającej walidacje.

        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Every field must be filled!");
            alert.showAndWait();
        } else if (!handlePasswordValidation(password)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Password does not met requirements! Use 8 characters with at least one number, special character and capital letter . . .");
            alert.showAndWait();
        } else if (handleSameName(login)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User with such name already exists . . . ");
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
                    handleDefaultBalance(conn, login);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Domyślna ilość pieniędzy - 0 przy dodaniu usera + limit 999 999 999 MLN USD

    private static void handleDefaultBalance(Connection conn, String login) throws SQLException {
        String sql_query_1 = "SELECT userID FROM users WHERE loginHash = ?";
        String sql_query_2 = "INSERT INTO funds (userID, balance, fundsLimit) VALUES (?, ?, ?)";
        PreparedStatement stmt1 = conn.prepareStatement(sql_query_1);
        stmt1.setString(1, login);
        ResultSet result = stmt1.executeQuery();

        while (result.next()) {
            int userID = result.getInt("userID");
            PreparedStatement stmt2 = conn.prepareStatement(sql_query_2);
            stmt2.setInt(1, userID);
            stmt2.setDouble(2, 0);
            stmt2.setDouble(3, MAX_BALANCE);
            stmt2.executeUpdate();
        }
    }

    // Metoda walidacji hasła (długość oraz wystapienie znaku specjalnego dużej litery i cyfry)

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

    // Metoda walidacji czy jest istniejący użytkownik o takiej samej nazwie.

    private boolean handleSameName(String username) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql_query = "SELECT userID FROM users WHERE loginHash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return true;
            }
            return false;
        }
    }

    @FXML
    private void handlePreviousPanelClick() throws IOException {
        Stage stage = (Stage) previousPageButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/admin/AdminPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }
}

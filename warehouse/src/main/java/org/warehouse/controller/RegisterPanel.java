package org.warehouse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.warehouse.model.Nationality;
import org.warehouse.util.DBConnection;

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
    private void initialize() {
        List<Nationality> nationalities = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM nationality WHERE NOT nationalityID = 1";
            PreparedStatement loginst = conn.prepareStatement(sql);
            ResultSet rs = loginst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("nationalityID");
                String nationality = rs.getString("country");
                nationalities.add(new Nationality(id,nationality));
            }
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Nationality> nationalityOptions = FXCollections.observableArrayList(nationalities);
        registerNationality.setItems(nationalityOptions);
        for(Nationality nationality : nationalities){
            System.out.println(nationality);
        }

    }

    @FXML
    private void handleRegister() {
        String login = registerLogin.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        int nationalityID = registerNationality.getSelectionModel().getSelectedItem().getNationalityID();

        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wszystkie pola muszą być wypełnione.");
            alert.showAndWait();
        }else{
            String sql = "INSERT INTO users (loginHash, password, creationDate, nationalityID, isAdmin) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, login);
                stmt.setString(2, password);
                stmt.setDate(3, Date.valueOf(LocalDate.now()));
                stmt.setInt(4,nationalityID);
                stmt.setBoolean(5,false);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Wstawiono nowy rekord!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

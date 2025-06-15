package org.warehouse.controller.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.warehouse.model.ManageFundsUserView;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageFundsController {

    // Maksymalna ilość salda na koncie, żeby nie przekroczyć tego miliarda dolarów (PRZYKŁADOWO).

    public static final int MAX_BALANCE = 999999999;
    @FXML
    private TableView<ManageFundsUserView> userTable;
    @FXML
    private TableColumn<ManageFundsUserView, Integer> idCol;
    @FXML
    private TableColumn<ManageFundsUserView, String> loginCol;
    @FXML
    private TableColumn<ManageFundsUserView, LocalDateTime> dateCol;
    @FXML
    private TableColumn<ManageFundsUserView, String> nationalityCol;
    @FXML
    private TableColumn<ManageFundsUserView, Double> balanceCol;
    @FXML
    private Button previousPageButton;
    @FXML
    private TextField balanceField;

    // Lista obserwowalna użytkowników żeby wyświetlić ich w tabeli.

    private final ObservableList<ManageFundsUserView> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("loginHash"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        nationalityCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Formatka do wyświetlania daty.

        dateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        // Formatka do poprawnego wyświetlania funduszy (żeby nie było czegoś typu 8.3E5 itp).

        balanceCol.setCellFactory(column -> new TableCell<ManageFundsUserView, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", item));
                }
            }
        });

        // Załadowanie i wyświetlenie userów w tabeli.

        handleViewUsers();
        userTable.setItems(userList);

        // Walidacja regexem, chcemy cyfry i tylko cyfry.

        balanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    balanceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    // Chcemy pobrać z bazy danych userów (z wyjątkiem Admina ze względów bezpieczeństwa)
    // wraz z ich narodowością saldem datą utworzenia itd.

    @FXML
    private void handleViewUsers() {
        String sql = "SELECT users.userID, users.loginHash, users.creationDate, nationality.country, funds.balance FROM users JOIN nationality ON users.nationalityID = nationality.nationalityID JOIN funds ON funds.userID = users.userID WHERE users.userID != 1";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet result = stmt.executeQuery(sql)) {
            while (result.next()) {
                ManageFundsUserView users = new ManageFundsUserView(result.getInt("userID"), result.getString("loginHash"), result.getTimestamp("creationDate").toLocalDateTime(), result.getString("country"), result.getDouble("balance"));
                userList.add(users);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddFunds() {
        String inputText = balanceField.getText().trim();

        // Walidacja - co jeśli podamy 0$.

        if (inputText.isEmpty() || inputText.equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid balance . . .");
            alert.showAndWait();
            return;
        }

        // Walidacja - co jeśli nie wybraliśmy usera któremu chcemy dodać fundusze. (klik na usera na tabeli)

        ManageFundsUserView selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User has not been chosen . . .");
            alert.showAndWait();
            return;
        }

        // Wykroczenie poza MAX_BALANCE - nie możemy mieć takiej sytuacji.

        try {
            double amountToAdd = Double.parseDouble(inputText);
            double currentBalance = selectedUser.getBalance();
            double newBalance = currentBalance + amountToAdd;

            if (amountToAdd <= 0 || newBalance > MAX_BALANCE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Amount exceeds maximum allowed balance . . .");
                alert.showAndWait();
                return;
            }

            // Update salda do bazy (UPDATE).

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE funds SET balance = ? WHERE userID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, newBalance);
                stmt.setInt(2, selectedUser.getUserID());
                int rowsUpdated = stmt.executeUpdate();

                // Jeśli UPDATE się powiódl to odświeżamy widok tabeli

                if (rowsUpdated > 0) {
                    selectedUser.setBalance(newBalance);
                    userTable.refresh();
                }

                // Dalej już obsługi błędów, jeśli wartość to nie liczba, update się nie powiedzie do bazy.
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid number format!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Database error, cannot update funds . . .");
            alert.showAndWait();
        }
    }

    // Obsługa przycisku wstecz - przejście do panelu poprzedniego (Obsługi przycisków wylogowywania, przechodzenia będą już kopiowane z poprzednich klas).

    @FXML
    private void handlePreviousPanelClick() throws IOException {
        Stage stage = (Stage) previousPageButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/admin/AdminPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}




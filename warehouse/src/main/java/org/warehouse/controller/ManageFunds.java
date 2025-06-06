package org.warehouse.controller;
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
//import org.warehouse.model.Nationality;
//import org.warehouse.model.RemoveUserView;
// Do naprawienia bug z infinity money glitch.
import org.warehouse.util.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageFunds {
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
    private Button previousPanel;
    @FXML
    private TextField balance;

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
        balanceCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f $", item)); // np. 1 000 000,00
                }
            }
        });
        handleViewUsers();
        userTable.setItems(userList);

        balance.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    balance.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void handleViewUsers() {
        String sql = "SELECT users.userID, users.loginHash, users.creationDate, nationality.country, funds.balance FROM users JOIN nationality ON users.nationalityID = nationality.nationalityID JOIN funds ON funds.userID = users.userID WHERE users.userID != 1";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ManageFundsUserView users = new ManageFundsUserView(
                        rs.getInt("userID"),
                        rs.getString("loginHash"),
                        rs.getTimestamp("creationDate").toLocalDateTime(),
                        rs.getString("country"),
                        rs.getDouble("balance")
                );
                userList.add(users);
            }
        } catch (SQLException e) {
            // Lepszy widok w razie errora. printStackTrace.
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddFunds() {
        String inputText = balance.getText().trim();
        // Walidacja - co jeśli podamy 0$.
        if (inputText.isEmpty() || inputText.equals("0")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid balance . . .");
            alert.showAndWait();
            return;
        }
        // Walidacja - co jeśli nie wybraliśmy usera któremu chcemy dodać funduse.
        ManageFundsUserView selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User has not been chosen . . .");
            alert.showAndWait();
            return;
        }
        // Wykroczenie poza MAX_BALANCE.
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

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE funds SET balance = ? WHERE userID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, newBalance);
                stmt.setInt(2, selectedUser.getUserID());
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    selectedUser.setBalance(newBalance);
                    userTable.refresh();
                }
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
    // Obsługa przycisku wstecz - przejście do panelu poprzedniego.
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




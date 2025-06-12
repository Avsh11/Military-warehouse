package org.warehouse.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.warehouse.model.RemoveUserView;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RemoveUserController {

    @FXML
    private TableView<RemoveUserView> userTable;
    @FXML
    private TableColumn<RemoveUserView, Integer> idCol;
    @FXML
    private TableColumn<RemoveUserView, String> loginCol;
    @FXML
    private TableColumn<RemoveUserView, LocalDateTime> dateCol;
    @FXML
    private TableColumn<RemoveUserView, String> nationalityCol;
    @FXML
    private Button previousPageButton;

    private final ObservableList<RemoveUserView> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("loginHash"));
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

        nationalityCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        // Wywołanie metody handleViewUsers - dajemy wyniki z bazy do listy obserwowalnej.

        handleViewUsers();
        userTable.setItems(userList);
    }

    // Metoda do wyświetlania informacji, chcemy mieć podstawowe informacje o userze którego chcemy usunąć.

    @FXML
    private void handleViewUsers() {
        String sql = "SELECT users.userID, users.loginHash, users.creationDate, nationality.country FROM users JOIN nationality ON users.nationalityID = nationality.nationalityID WHERE users.userID != 1";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet result = stmt.executeQuery(sql)) {
            while (result.next()) {
                RemoveUserView users = new RemoveUserView(result.getInt("userID"), result.getString("loginHash"), result.getTimestamp("creationDate").toLocalDateTime(), result.getString("country"));
                userList.add(users);
            }
        } catch (SQLException e) {

            // Lepszy widok w razie errora. printStackTrace.

            e.printStackTrace();
        }
    }

    private boolean handleRemoveUserMethod(int userID) {
        String sql = "DELETE FROM users WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // true jeśli usunęło co najmniej jeden wiersz.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Usuwanie usera plus jego funduszy.

    // Plus obsługa buttona do usuwania. Po kliknięciu wywołamy tę metodę (OnAction="#metoda").

    @FXML
    private void handleRemoveUser() {
        RemoveUserView selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            boolean fundsRemoved = handleRemoveUserFunds();
            boolean userRemoved = handleRemoveUserMethod(selectedUser.getUserID());
            if (userRemoved && fundsRemoved) {
                userTable.getItems().remove(selectedUser);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("User could not be deleted . . .");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User has not been choosen . . .");
            alert.showAndWait();
        }
    }

    // Usuwanie funduszy. Nie ma usera nie ma funduszy.

    private boolean handleRemoveUserFunds() {
        RemoveUserView selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String sql = "DELETE FROM funds WHERE userID = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, selectedUser.getUserID());
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
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

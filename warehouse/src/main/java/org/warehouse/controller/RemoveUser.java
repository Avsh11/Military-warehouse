package org.warehouse.controller;
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

public class RemoveUser {
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
    private Button previousPanel;
    @FXML
    private Button removeUserButton;

    private final ObservableList<RemoveUserView> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("loginHash"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // lub "dd.MM.yyyy" bez godziny
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
    // Metoda handleViewUsers
    @FXML
    private void handleViewUsers(){
        String sql = "SELECT users.userID, users.loginHash, users.creationDate, nationality.country FROM users JOIN nationality ON users.nationalityID = nationality.nationalityID WHERE users.userID != 1";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                RemoveUserView users = new RemoveUserView(
                        rs.getInt("userID"),
                        rs.getString("loginHash"),
                        rs.getTimestamp("creationDate").toLocalDateTime(),
                        rs.getString("country")
                );
                userList.add(users);
            }
        } catch (SQLException e) {
            // Lepszy widok w razie errora. printStackTrace.
            e.printStackTrace();
        }
    }

    private boolean handleRemoveUserMethod(int userID){
        String sql = "DELETE FROM users WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Usuwanie usera plus jego funduszy.
    @FXML
    private void handleRemoveUser(){
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
                alert.setContentText("User could not be deleted");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("User has not been choosen");
            alert.showAndWait();
        }
    }

    private boolean handleRemoveUserFunds(){
        RemoveUserView selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String sql_query_1 = "DELETE FROM funds WHERE userID = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql_query_1)) {

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
    // Button do przejścia wstecz - przekopiowane z AdminPanel
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

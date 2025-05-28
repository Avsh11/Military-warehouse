package org.warehouse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.warehouse.model.Users;
import org.warehouse.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class RemoveUser {
    @FXML
    private TableView<Users> userTable;
    @FXML
    private TableColumn<Users, Integer> idCol;
    @FXML
    private TableColumn<Users, String> loginCol;
    @FXML
    private TableColumn<Users, LocalDateTime> dateCol;
    @FXML
    private TableColumn<Users, Integer> nationalityCol;
    @FXML
    private TableColumn<Users, Boolean> adminCol;

    private final ObservableList<Users> userList = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("loginHash"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        nationalityCol.setCellValueFactory(new PropertyValueFactory<>("nationalityID"));
        // Patrz model Users i Gettery & Settery !
        adminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));

        handleViewUsers();
        userTable.setItems(userList);
    }

    @FXML
    private void handleViewUsers(){
        String sql = "SELECT userID, loginHash, creationDate, nationalityID, isAdmin FROM users";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Users users = new Users(
                        rs.getInt("userID"),
                        rs.getString("loginHash"),
                        rs.getTimestamp("creationDate").toLocalDateTime(),
                        rs.getInt("nationalityID"),
                        rs.getBoolean("isAdmin")
                );
                userList.add(users);
            }

        } catch (SQLException e) {
            // Lepszy widok w razie errora. printStackTrace.
            e.printStackTrace();
        }
    }

    private void handleRemoveUser(){

    }
}

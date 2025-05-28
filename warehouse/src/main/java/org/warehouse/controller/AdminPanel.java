package org.warehouse.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanel {
    @FXML
    private Button addUser;
    @FXML
    private Button manageUsers;
    @FXML
    private Button manageFunds;
    @FXML
    private Button logout;

    // Metody są osobno ponieważ daje mi to większą przejrzystość, zdaję sobie sprawę, że nie powinienem pisać
    // w ten sposób. Tak jak wspomniałem zapisuję te metody osobno tylko dla przejrzystości kodu w przypadkach
    // wystąpień błędów

    @FXML
    private void handleAddUser() throws IOException {
        Stage stage = (Stage) addUser.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RegisterPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRemoveUser() throws IOException {
        Stage stage = (Stage) manageUsers.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RemoveUser.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleManageFunds() throws IOException {
        Stage stage = (Stage) manageFunds.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/ManageFundsPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogout() throws IOException {
        Stage stage = (Stage) logout.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/LoginPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }
}




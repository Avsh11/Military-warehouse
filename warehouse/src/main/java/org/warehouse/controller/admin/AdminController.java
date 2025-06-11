package org.warehouse.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.warehouse.util.UserInfo;

import java.io.IOException;

public class AdminController {

    @FXML
    private Button addUserButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageFundsButton;
    @FXML
    private Button logoutButton;

    // Metody są osobno ponieważ daje mi to większą przejrzystość, zdaję sobie sprawę, że nie powinienem pisać
    // w ten sposób. Tak jak wspomniałem zapisuję te metody osobno tylko dla przejrzystości kodu w przypadkach
    // wystąpień błędów

    // Każda z metod służy tylko i wyłącznie do przenoszenia użytkownika - ADMINA do kolejnych paneli
    // funkcyjnych (za pomocą przycisków).

    // Pobieramy aktualne okno w którym znajduje się przycisk o określonym fx:id. Chodzi po prostu o to żeby
    // zmienić zawartość okna po czym ładujemy nowy plik FXML który zawiera widok
    // Tworzymy scene na podstawie tego widoku a potem ustawiamy ją jako nową zawartość okna
    // Na końcu odświeżamy okno żeby wyświetlić zmiany.

    @FXML
    private void handleAddUserClick() throws IOException {
        Stage stage = (Stage) addUserButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/admin/RegisterPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRemoveUserClick() throws IOException {
        Stage stage = (Stage) manageUsersButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/admin/RemoveUserPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleManageFundsClick() throws IOException {
        Stage stage = (Stage) manageFundsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/admin/ManageFundsPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogoutClick() throws IOException {
        UserInfo.userID = 0;
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/LoginPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }
}




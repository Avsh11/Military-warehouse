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

    // Każda z metod służy tylko i wyłącznie do przenoszenia użytkownika - ADMINA do kolejnych paneli
    // funkcyjnych.

    // Pobieramy aktualne okno w którym znajduje się przycisk o określonym fx:id. Chodzi po prostu o to żeby
    // zmienić zawartość okna po czym ładujemy nowy plik FXML który zawiera widok
    // Tworzymy scene na podstawie tego widoku a potem ustawiamy ją jako nową zawartość okna
    // Na końcu odświeżamy okno żeby wyświetlić zmiany.

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RemoveUserPanel.fxml"));
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




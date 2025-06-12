package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductCategoryController {

    // nationalityID będzie przechowywać ID kraju wybranego w UserController.
    // Trzeba wiedzieć dla jakiego kraju filtrować spółki produkty.

    private int nationalityID;
    @FXML
    private Button previousPageButton;
    @FXML
    private Button categoryLandButton;
    @FXML
    private Button categoryAirButton;

    // Tu przekazujemy nationalityID, ta publiczna metoda jest wywoływana w klasie UserController
    // zaraz po załadowaniu żeby przenieść to nationalityID.
    // Teraz to ID jest dostępne w tej klasie.

    public void handleSetCountry(int nationalityID) {
        this.nationalityID = nationalityID;
    }

    // Tutaj chcemy załadować widok z producentami (FXML - ManufacturerPanel) i wczesniej
    // trzeba przekazać mu informacje takie jak kraj i kategoria
    // Czyli pobieramy kontroler i wywołujemy metodę w tym przypadku
    // handleSetManufacturer przekazując dwa parametry: kategorie i nationalityID.
    // Kategoria air albo land tak jak miało być w tym enumie.

    private void handleSetManufacturerCategoryPanel(String category, Stage stage, int nationalityID) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ManufacturerPanel.fxml"));
            Parent primaryFXML = fxmlLoader.load();
            ManufacturerController controller = fxmlLoader.getController();
            controller.handleSetManufacturer(category, nationalityID);
            Scene scene = new Scene(primaryFXML);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obsługa buttonów - wywołujemy główną metodę i przekazujemy jej dane. Tą kategorię i nationalityID przechowywane wcześniej już.

    @FXML
    private void handleCategoryLandClick() {
        Stage stage = (Stage) categoryLandButton.getScene().getWindow();
        handleSetManufacturerCategoryPanel("Land", stage, nationalityID);
    }

    @FXML
    private void handleCategoryAirClick() {
        Stage stage = (Stage) categoryAirButton.getScene().getWindow();
        handleSetManufacturerCategoryPanel("Air", stage, nationalityID);
    }

    // Powrót do poprzedniego panelu, nic się nie zmienia. Z palca nie opłaca się ponownie pisać tych metod ;).

    @FXML
    private void handlePreviousPanelClick() throws IOException {
        Stage stage = (Stage) previousPageButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/UserPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

}

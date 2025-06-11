package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductCategoryController {

    private int nationalityID;
    @FXML
    private Button previousPageButton;
    @FXML
    private Button categoryLandButton;
    @FXML
    private Button categoryAirButton;


    public void handleSetCountry(int nationalityID) {
        this.nationalityID = nationalityID;
    }

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

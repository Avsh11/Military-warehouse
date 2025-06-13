package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManufacturerController {

    @FXML
    public Button firstCompanyButton;
    @FXML
    public Button secondCompanyButton;

    private int nationalityID;
    private String productType;

    // Pola do przechowywania idków producentów.

    private int idCompany1 = 0;
    private int idCompany2 = 0;

    public void handleSetManufacturer(String productType, int nationalityID) {

        // Dostęp do parametrów w innych metodach.

        this.nationalityID = nationalityID;
        this.productType = productType;

        // Z bazy chcemy pobrać id producenta jego narodowość czy firma jest z USA i wiadomo nazwę
        // do wyświetlenia.

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT manufacturerID, name, nationalityID, manufacturerType FROM Manufacturers WHERE nationalityID = ? AND manufacturerType = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nationalityID);
            stmt.setString(2, productType);
            ResultSet result = stmt.executeQuery();

            int i = 0;
            String company1 = "";
            String company2 = "";

            while (result.next()) {
                if (i == 0) {
                    company1 = result.getString(2);
                    idCompany1 = result.getInt(1);
                    i++;
                } else {
                    company2 = result.getString(2);
                    idCompany2 = result.getInt(1);
                    break;
                }
            }

            // Chcemy ustawić name na buttonie.

            firstCompanyButton.setText(company1);
            secondCompanyButton.setText(company2);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // IOException jak wystąpi problem podczas komunikacji z systemem w tym przypadku jak nie da rady
    // znaleźć pliku FXML. Kod wywołujący metodę wie jakich wyjątków może się spodziewać.
    // Książka str 226 o klauzuli throws.

    @FXML
    private void handleFirstCompany() throws IOException {
        Stage stage = (Stage) firstCompanyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ProductPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        ProductController controller = fxmlLoader.getController();
        controller.handleViewProducts(idCompany1);
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleSecondCompany() throws IOException {
        Stage stage = (Stage) secondCompanyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ProductPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        ProductController controller = fxmlLoader.getController();
        controller.handleViewProducts(idCompany2);
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

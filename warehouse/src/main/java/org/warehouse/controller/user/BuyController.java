package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.warehouse.model.Products;
import org.warehouse.util.DBConnection;
import org.warehouse.util.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BuyController {

    @FXML
    public ImageView productImage;
    @FXML
    public Label buyNameLabel;
    @FXML
    public Label buyPriceLabel;
    @FXML
    public Label buyStockLabel;
    @FXML
    public TextField buyField;
    @FXML
    public Button buyButton;
    @FXML
    public Label finalPriceLabel;
    @FXML
    public Button previousPageButton;

    private double price = 0;

    // Obiekt produktu.

    private Products product;

    // Podobnie jak w dodawaniu środków chcemy mieć cyfry i tylko cyfry stąd regex.

    @FXML
    private void initialize() {
        buyField.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d*")) {
                buyField.setText(newText.replaceAll("[^\\d]", ""));
            }
            handleSetPrice();
        });
    }

    private String handleFormatPrice(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        return formatter.format(price).replace(",", ".");
    }

    public void handleSetProduct(Products product) {

        // labele ustawiamy na podstawie danych z produktu.

        this.product = product;
        buyNameLabel.setText(product.getName());
        buyPriceLabel.setText(handleFormatPrice(product.getPricePerUnit()) + " $"); // Tu już formatka
        // z użyciem metody i DecimalFormatem więc już elegancko będzie.
        buyStockLabel.setText(String.valueOf(product.getInStock()));

        // Ładowanie obrazka produktu + obsługa przypadku gdy nie będzie mogło znaleźć obrazka lub załadować.

        try {
            String imagePath = "/image/products/" + product.getPhotoName();
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null) {
                System.err.println("Could not find an image: " + imagePath + " inside directory");
                productImage.setImage(null);
            } else {
                Image image = new Image(is);
                productImage.setImage(image);
            }
        } catch (Exception e) {
            productImage.setImage(null);
            System.err.println("Could not load an image: " + product.getPhotoName());
            e.printStackTrace();
        }
    }

    // Obliczanie i wyświetlanie nowej ceny gdy user zmienia ilość tego co chce kupić.
    // price = product.getPricePerUnit() * amount; cena za sztukę razy ilość.

    private void handleSetPrice() {
        String text = buyField.getText().trim();
        if (text.isEmpty()) {
            finalPriceLabel.setText("");
            return;
        }
        try {
            int amount = Integer.parseInt(text);
            if (product != null) {
                price = product.getPricePerUnit() * amount;
                finalPriceLabel.setText(handleFormatPrice(price) + " $");
            }
        } catch (NumberFormatException e) {
            finalPriceLabel.setText("Błędna wartość");
        }
    }

    // W obsłudze buttona do kupowania cała logika walidacje, sprawdzanie środkow, zapytania do bazy (inserty,updaty).

    @FXML
    private void handleBuyButtonClick() {
        int amount = Integer.parseInt(buyField.getText().trim());

        // Jak chcemy kupić więcej niż można = alert.

        if (amount > product.getInStock()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Such amount is not currently in stock . . .");
            alert.showAndWait();
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM funds WHERE userID = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, UserInfo.userID);
            ResultSet resultSet = stmt.executeQuery();

            double wallet = 0;

            while (resultSet.next()) {
                wallet = resultSet.getDouble("balance");
            }
            if (wallet < price) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Funds does not match price");
                alert.showAndWait();
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime deliveryDate = LocalDateTime.now().plusMonths(1); // Dzisiejszy date + miesiąc.

            // IntelliJ nie analizuje dynamicznych zapytań z przypisania do zmiennej sql w tym przypadku.
            // Usunąć zmienną sql i przypisać zapytanie do PreparedStatement.

            // Wstawianie informacji o zamówieniu do tabeli orders. (Potem chcemy jednak wyświetlać zamówienia).

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO orders (productID, userID, orderQuantity, orderPrice, paymentDate, deliveryDate) VALUES (?, ?, ?, ?, ?, ?)");
            stmt1.setInt(1, product.getProductID());
            stmt1.setInt(2, UserInfo.userID);
            stmt1.setInt(3, amount);
            stmt1.setDouble(4, price);
            stmt1.setString(5, LocalDateTime.now().toString());
            stmt1.setString(6, deliveryDate.toString());
            stmt1.executeUpdate();

            PreparedStatement stmt2 = conn.prepareStatement("UPDATE funds SET balance = ? WHERE userID = ?");
            stmt2.setDouble(1, wallet - price);
            stmt2.setInt(2, UserInfo.userID);
            stmt2.executeUpdate();

            PreparedStatement stmt3 = conn.prepareStatement("UPDATE products SET inStock = ? WHERE productID = ?");
            stmt3.setInt(1, product.getInStock() - amount);
            stmt3.setInt(2, product.getProductID());
            stmt3.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thanks for purchase");
            alert.setHeaderText(null);
            alert.setContentText("Thanks for purchase\n" + "Delivery date: " + deliveryDate.format(formatter));
            alert.showAndWait();

            // Plus obsługa buttona standardowo (panel poprzedni/głowny).

            Stage stage = (Stage) previousPageButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ProductPanel.fxml"));
            Parent primaryFXML = fxmlLoader.load();
            ProductController controller = fxmlLoader.getController();
            controller.handleViewProducts(product.getManufacturerID());
            Scene scene = new Scene(primaryFXML);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handlePreviousPanelClick() throws IOException {
        Stage stage = (Stage) previousPageButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/ProductPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        ProductController controller = fxmlLoader.getController();
        controller.handleViewProducts(product.getManufacturerID());
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

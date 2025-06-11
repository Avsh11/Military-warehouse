package org.warehouse.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.warehouse.model.Products;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    @FXML
    public Label p1nameLabel;
    @FXML
    public Label p2nameLabel;
    @FXML
    public Label p1descLabel;
    @FXML
    public Label p1priceLabel;
    @FXML
    public Label p1stockLabel;
    @FXML
    public Label p2descLabel;
    @FXML
    public Label p2priceLabel;
    @FXML
    public Label p2stockLabel;
    @FXML
    public Button p1buyButton;
    @FXML
    public Button p2buyButton;
    @FXML
    public ImageView p1imageImage;
    @FXML
    public ImageView p2imageImage;
    @FXML
    private Button previousPageButton;
    private int manufacturerID;

    private List<Products> products = new ArrayList<>();

    public void handleViewProducts(int manufacturerID) {

        this.manufacturerID = manufacturerID;

        products.clear(); //ADD

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM products WHERE idManufacturer = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, manufacturerID);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Products product = new Products();
                product.setProductID(result.getInt("productID"));
                product.setName(result.getString("name"));
                product.setPricePerUnit(result.getDouble("pricePerUnit"));
                product.setManufacturerID(result.getInt("idManufacturer"));
                product.setInStock(result.getInt("inStock"));
                product.setProductDescription(result.getString("productDescription"));
                product.setPhotoName(result.getString("photoName"));
                products.add(product);
            }
            p1nameLabel.setText(products.get(0).getName());
            p1descLabel.setText(products.get(0).getProductDescription());
            p1priceLabel.setText(String.format("%.2f $", products.get(0).getPricePerUnit()));
            p1stockLabel.setText(String.valueOf(products.get(0).getInStock()));

            String p1Photo = products.get(0).getPhotoName();
            if (p1Photo != null && !p1Photo.isEmpty()) {
                try {
                    Image image1 = new Image(getClass().getResourceAsStream("/image/products/" + p1Photo));
                    p1imageImage.setImage(image1);
                } catch (Exception e) {
                    p1imageImage.setImage(null);
                }
            } else {
                p1imageImage.setImage(null);
            }

            p2nameLabel.setText(products.get(1).getName());
            p2descLabel.setText(products.get(1).getProductDescription());
            p2priceLabel.setText(String.format("%.2f $", products.get(1).getPricePerUnit()));
            p2stockLabel.setText(String.valueOf(products.get(1).getInStock()));

            String p2Photo = products.get(1).getPhotoName();
            if (p2Photo != null && !p2Photo.isEmpty()) {
                try {
                    Image image2 = new Image(getClass().getResourceAsStream("/image/products/" + p2Photo));
                    p2imageImage.setImage(image2);
                } catch (Exception e) {
                    p2imageImage.setImage(null);
                }
            } else {
                p2imageImage.setImage(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @FXML
    private void handleBuyP1Click() throws IOException {
        Stage stage = (Stage) p1buyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/BuyPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        BuyController controller = fxmlLoader.getController();
        controller.setProduct(products.get(0));
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleBuyP2Click() throws IOException {
        Stage stage = (Stage) p2buyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/user/BuyPanel.fxml"));
        Parent primaryFXML = fxmlLoader.load();
        BuyController controller = fxmlLoader.getController();
        controller.setProduct(products.get(1));
        Scene scene = new Scene(primaryFXML);
        stage.setScene(scene);
        stage.show();
    }
}

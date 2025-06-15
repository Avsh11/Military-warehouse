package org.warehouse.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.warehouse.model.OrderHistoryAdminView;
import org.warehouse.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderHistoryAdminController {

    @FXML
    private TableView<OrderHistoryAdminView> orderTable;
    @FXML
    private TableColumn<OrderHistoryAdminView, Integer> orderIDCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, String> userLoginCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, String> productNameCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, Integer> quantityCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, Double> priceCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, LocalDateTime> paymentDateCol;
    @FXML
    private TableColumn<OrderHistoryAdminView, LocalDateTime> deliveryDateCol;
    @FXML
    private Button previousPageButton;

    @FXML
    public void initialize() {
        ObservableList<OrderHistoryAdminView> data = FXCollections.observableArrayList();

        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        userLoginCol.setCellValueFactory(new PropertyValueFactory<>("userLogin"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("orderQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("orderPrice"));
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        priceCol.setCellFactory(column -> new TableCell<OrderHistoryAdminView, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%,.2f", item));
            }
        });

        paymentDateCol.setCellFactory(column -> new TableCell<OrderHistoryAdminView, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        deliveryDateCol.setCellFactory(column -> new TableCell<OrderHistoryAdminView, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT orderID, name, loginHash, orderQuantity, orderPrice, paymentDate, deliveryDate\n" +
                    "FROM Orders\n" +
                    "INNER JOIN Products ON Orders.productID = Products.productID\n" +
                    "INNER JOIN Users ON Orders.userID = Users.userID\n" +
                    "ORDER BY paymentDate DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                OrderHistoryAdminView order = new OrderHistoryAdminView();
                order.setOrderID(result.getInt("orderID"));
                order.setProductName(result.getString("name"));
                order.setUserLogin(result.getString("loginHash"));
                order.setOrderQuantity(result.getInt("orderQuantity"));
                order.setOrderPrice(result.getDouble("orderPrice"));
                order.setPaymentDate(result.getTimestamp("paymentDate").toLocalDateTime());
                order.setDeliveryDate(result.getTimestamp("deliveryDate").toLocalDateTime());
                data.add(order);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        orderTable.setItems(data);
    }

    @FXML
    private void  handlePreviousPanelClick() throws IOException {
        Stage stage = (Stage) previousPageButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/admin/AdminPanel.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }
}


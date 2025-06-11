package org.warehouse.controller.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.warehouse.model.OrderHistoryUserView;
import org.warehouse.util.DBConnection;
import org.warehouse.util.UserInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderHistoryController {

    @FXML
    private TableView<OrderHistoryUserView> orderTable;
    @FXML
    private TableColumn<OrderHistoryUserView, Integer> orderIDCol;
    @FXML
    private TableColumn<OrderHistoryUserView, String> productNameCol;
    @FXML
    private TableColumn<OrderHistoryUserView, Integer> quantityCol;
    @FXML
    private TableColumn<OrderHistoryUserView, Double> priceCol;
    @FXML
    private TableColumn<OrderHistoryUserView, LocalDateTime> paymentDateCol;
    @FXML
    private TableColumn<OrderHistoryUserView, LocalDateTime> deliveryDateCol;
    @FXML
    public Button previousPageButton;

    @FXML
    public void initialize() {

        ObservableList<OrderHistoryUserView> data = FXCollections.observableArrayList();

        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("orderQuantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("orderPrice"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        deliveryDateCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        // Formatowanie kolejno ceny zamówienia, daty przelewu, dostawy.

        priceCol.setCellFactory(column -> new TableCell<OrderHistoryUserView, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", item));
                }
            }
        });

        paymentDateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        deliveryDateCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt1 = conn.prepareStatement("SELECT orderID, name, orderQuantity, orderPrice, paymentDate, deliveryDate " +
                    "FROM orders " +
                    "INNER JOIN products " +
                    "ON orders.productID = products.productID " +
                    "WHERE userID = ?");
            stmt1.setInt(1, UserInfo.userID);
            ResultSet result = stmt1.executeQuery();
            while (result.next()) {
                OrderHistoryUserView order = new OrderHistoryUserView();
                order.setOrderID(result.getInt("orderID"));
                order.setName(result.getString("name"));
                order.setOrderQuantity(result.getInt("orderQuantity"));
                order.setOrderPrice(result.getDouble("orderPrice"));
                order.setPaymentDate(result.getTimestamp("paymentDate").toLocalDateTime());
                order.setDeliveryDate(result.getTimestamp("deliveryDate").toLocalDateTime());
                data.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderTable.setItems(data);
        for (OrderHistoryUserView order : data) {
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
}

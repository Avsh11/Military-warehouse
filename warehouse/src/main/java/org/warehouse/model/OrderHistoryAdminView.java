package org.warehouse.model;

import java.time.LocalDateTime;

public class OrderHistoryAdminView {
    private int orderID;
    private String productName;
    private String userLogin;
    private int orderQuantity;
    private double orderPrice;
    private LocalDateTime paymentDate;
    private LocalDateTime deliveryDate;

    public OrderHistoryAdminView() {

    }

    @Override
    public String toString() {
        return "OrderHistoryAdminView{" +
                "orderID=" + orderID +
                ", productName='" + productName + '\'' +
                ", userLogin='" + userLogin + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", orderPrice=" + orderPrice +
                ", paymentDate=" + paymentDate +
                ", deliveryDate=" + deliveryDate +
                '}';
    }

    public OrderHistoryAdminView(int orderID, String productName, String userLogin, int orderQuantity, double orderPrice, LocalDateTime paymentDate, LocalDateTime deliveryDate) {
        this.orderID = orderID;
        this.productName = productName;
        this.userLogin = userLogin;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
        this.paymentDate = paymentDate;
        this.deliveryDate = deliveryDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}

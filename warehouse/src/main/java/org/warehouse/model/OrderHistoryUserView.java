package org.warehouse.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class OrderHistoryUserView {
    private int orderID;
    private String name;
    private int orderQuantity;
    private double orderPrice;
    private LocalDateTime paymentDate;
    private LocalDateTime deliveryDate;

    public OrderHistoryUserView() {

    }

    @Override
    public String toString() {
        return "OrderHistoryUserView{" +
                "orderID=" + orderID +
                ", name='" + name + '\'' +
                ", orderQuantity=" + orderQuantity +
                ", orderPrice=" + orderPrice +
                ", paymentDate=" + paymentDate +
                ", deliveryDate=" + deliveryDate +
                '}';
    }

    public OrderHistoryUserView(int orderID, String name, int orderQuantity, double orderPrice, LocalDateTime paymentDate, LocalDateTime deliveryDate) {
        this.orderID = orderID;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
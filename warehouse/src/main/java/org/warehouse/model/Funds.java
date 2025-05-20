package org.warehouse.model;

public class Funds {
    private int fundsID;
    private int userID;
    private double balance;
    private double fundsLimit;

    // Getters & Setters

    public int getFundsID() {
        return fundsID;
    }

    public void setFundsID(int fundsID) {
        this.fundsID = fundsID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFundsLimit() {
        return fundsLimit;
    }

    public void setFundsLimit(double fundsLimit) {
        this.fundsLimit = fundsLimit;
    }
}
